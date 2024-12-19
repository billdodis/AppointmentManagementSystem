import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { editUser, loginUser } from '../services/UserService';
import { validateToken } from '../services/TokenService';
import { jwtDecode } from 'jwt-decode';
import '../assets/styles/ChangeData.css';
import '../assets/styles/MainHeader.css';
import '../assets/styles/BackToHomeButton.css';
import '../assets/styles/Form.css';
import 'font-awesome/css/font-awesome.min.css';

const ChangeDataComponent = () => {
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    oldPassword: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [userId, setUserId] = useState('');
  const [timedOut, setTimedOut] = useState(null);
  const [emailError, setEmailError] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      const decodedToken = jwtDecode(localStorage.getItem('token'));
      const name = localStorage.getItem('name');
      setUserId(localStorage.getItem('id'));
      const email = decodedToken.sub;
      if (name && email) {
        const [firstName, lastName] = name.split(' ');
        setUserData((prevData) => ({
          ...prevData,
          firstName,
          lastName,
          email,
        }));
      }
    };
    const token = localStorage.getItem("token");
    if (token) {
      validateToken(token)
        .then((data) => {
          if (data === "Token is valid!") {
            fetchUserData();
          } else {
            setTimedOut(true);
            localStorage.removeItem("token");
            localStorage.removeItem("name");
            localStorage.removeItem("role");
            localStorage.removeItem("id");
          }
        })
    }
    else {
      setTimedOut(true);
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevData) => ({
      ...prevData,
      [name]: value,
    }));

    if (name === 'email') {
      setEmailError('');
    }
    if (error && value) {
      setError('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!userData.firstName || !userData.lastName || !userData.email || !userData.oldPassword) {
      setError('All fields except new password are required!');
      return;
    }
    if (!emailRegex.test(userData.email)) {
      setEmailError('Please enter a valid email address!');
      return;
    }
    if (userData.oldPassword.length < 4) {
      setError('Old password must be at least 4 characters long!');
      return;
    }
    if (userData.password && userData.password.length < 4) {
      setError('New password must be at least 4 characters long!');
      return;
    }

    try {
      console.log(userId);
      console.log(userData);
      const response = await editUser(userId, userData);
      if (response.error) {
        setError(response.error);
        return;
      }
      setSuccess('Your data has been successfully updated!');
      const loginDTO = userData.password
        ? { email: userData.email, password: userData.password }
        : userData.oldPassword
          ? { email: userData.email, password: userData.oldPassword }
          : null;
      // Log in the user, so that token is updated in case of email change. Also name updated in case of name change.
      try {
        const response = await loginUser(loginDTO);
        if (response.error) {
          setError(response.error);
        }
        else {
          localStorage.setItem("token", response.token);
          localStorage.setItem("name", response.name);
        }
      } catch (err) {
        setError(`${err.response?.data?.message || 'Error occurred'}`);
      }
      setTimeout(() => navigate('/'), 500);
    } catch (error) {
      console.error('Error updating user data:', error);
      setError('Failed to update your data. Please try again.');
    }
  };

  return (
    <div className="change-data-container">
      <h2>Edit my Data</h2>
      <div className="change-data-square">
        {success && <div className="success-message">{success}</div>}
        {error && <div className="error-message">{error}</div>}
        {emailError && <div className="error-message">{emailError}</div>}
        <form className="change-data-form" onSubmit={handleSubmit}>
          <div className="change-data-form-row">
            <label>First Name: </label>
            <input
              type="text"
              name="firstName"
              value={userData.firstName}
              onChange={handleChange}
              style={{ borderColor: !userData.firstName && error ? 'red' : '' }}
            />
          </div>

          <div className="change-data-form-row">
            <label>Last Name: </label>
            <input
              type="text"
              name="lastName"
              value={userData.lastName}
              onChange={handleChange}
              style={{ borderColor: !userData.lastName && error ? 'red' : '' }}
            />
          </div>

          <div className="change-data-form-row">
            <label>Email: </label>
            <input
              type="text"
              name="email"
              value={userData.email}
              onChange={handleChange}
              style={{ borderColor: emailError || (!userData.email && error) ? 'red' : '' }}
            />
          </div>

          <div className="change-data-form-row">
            <label>Old Password: </label>
            <input
              type="password"
              name="oldPassword"
              value={userData.oldPassword}
              onChange={handleChange}
              style={{ borderColor: (!userData.oldPassword && error) || (userData.password.length < 4 && error) ? 'red' : '' }}
              placeholder="Enter your current password!"
            />
          </div>

          <div className="change-data-form-row">
            <label>New Password (Optional): </label>
            <input
              type="password"
              name="password"
              value={userData.password}
              onChange={handleChange}
              style={{ borderColor: userData.password.length > 0 && userData.password.length < 4 && error ? 'red' : '' }}
              placeholder="Enter new password!"
            />
          </div>

          <button type="submit" className="change-data-submit">Update Data!</button>
        </form>
        <Link to="/" className="back-to-home-btn">
          <i className="fa fa-arrow-left back-icon"></i>
        </Link>
      </div>
      {timedOut && (
        <div className="popup">
          <div className="popup-content">
            <p><strong>Your session has expired!</strong><br />Please log in again, to view or manage your appointments.</p>
            <Link to="/login" className="confirm-btn">Login</Link>
          </div>
        </div>)}
    </div>
  );
};

export default ChangeDataComponent;