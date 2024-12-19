import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { addUser, loginUser } from '../services/UserService';
import '../assets/styles/SignUp.css';
import '../assets/styles/MainHeader.css';
import '../assets/styles/BackToHomeButton.css';
import '../assets/styles/Form.css';
import 'font-awesome/css/font-awesome.min.css';

const SignUpComponent = () => {
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [emailError, setEmailError] = useState('');

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevData) => ({
      ...prevData,
      [name]: value,
    }));

    if (name === "email") {
      setEmailError('');
    }
    if (error && value) {
      setError('');
    };
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!userData.firstName || !userData.lastName || !userData.password || !userData.email) {
      setError('All fields are required!');
      return;
    }
    if (!emailRegex.test(userData.email)) {
      setEmailError("Please enter a valid email address!");
      return;
    }
    if (userData.password.length < 4) {
      setError('Password must be at least 4 characters long!');
      return;
    }
    try {
      const response = await addUser(userData);
      if (response.error === "Email already exists!") {
        setError(response.error);
        return;
      }
      const loginResponse = await loginUser({
        email: userData.email,
        password: userData.password,
      });
      if (loginResponse.token) {
        localStorage.setItem("token", loginResponse.token);
        localStorage.setItem("name", loginResponse.name);
        localStorage.setItem("role", loginResponse.role);
        localStorage.setItem("id", loginResponse.id);
        setSuccess('User successfully registered!');
        setUserData({ firstName: '', lastName: '', email: '', password: '' });
        setTimeout(() => navigate('/'), 500);
      } else {
        setError('Error logging in after registration.');
      }
    } catch (error) {
      console.error("Error creating user:", error);
      setError(`Error creating user: ${error.response.status} - ${error.response.data.message}`);
    }
  };

  return (
    <div className="signup-container">
      <h2>Sign Up</h2>
      <div className="signup-square">
        {success && <div className="success-message">{success}</div>}
        {error && <div className="error-message">{error}</div>}
        {emailError && <div className="error-message">{emailError}</div>}
        <form className="signup-form" onSubmit={handleSubmit}>
          <div className="form-row">
            <label>First Name: </label>
            <input
              type="text"
              name="firstName"
              value={userData.firstName}
              onChange={handleChange}
              style={{ borderColor: !userData.firstName && error ? 'red' : '' }}
              placeholder="Enter your first name!"
            />
          </div>

          <div className="form-row">
            <label>Last Name: </label>
            <input
              type="text"
              name="lastName"
              value={userData.lastName}
              onChange={handleChange}
              style={{ borderColor: !userData.lastName && error ? 'red' : '' }}
              placeholder="Enter your last name!"
            />
          </div>

          <div className="form-row">
            <label>Email: </label>
            <input
              type="text"
              name="email"
              value={userData.email}
              onChange={handleChange}
              style={{ borderColor: emailError || (!userData.email && error) ? 'red' : '' }}
              placeholder="Enter your email!"
            />
          </div>

          <div className="form-row">
            <label>Password: </label>
            <input
              type="password"
              name="password"
              value={userData.password}
              onChange={handleChange}
              style={{ borderColor: (!userData.password && error) || (userData.password.length < 4 && error) ? 'red' : '' }}
              placeholder="Enter your password!"
            />
          </div>

          <button type="submit" className="signup-submit">Sign Up</button>
        </form>
        <Link to="/" className="back-to-home-btn">
          <i className="fa fa-arrow-left back-icon"></i>
        </Link>
      </div>
    </div>

  );
};

export default SignUpComponent;