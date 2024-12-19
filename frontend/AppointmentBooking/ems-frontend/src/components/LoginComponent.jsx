import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { loginUser } from '../services/UserService';
import '../assets/styles/Login.css';
import '../assets/styles/MainHeader.css';
import '../assets/styles/BackToHomeButton.css';
import '../assets/styles/Form.css';
import 'font-awesome/css/font-awesome.min.css';

const LoginComponent = () => {
  const [userData, setUserData] = useState({
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
    if (!userData.password || !userData.email) {
      setError('All fields are required!');
      return;
    }
    if (!emailRegex.test(userData.email)) {
      setEmailError("Please enter a valid email address.");
      return;
    }
    if (userData.password.length < 4) {
      setError('Password must be at least 4 characters long!');
      return;
    }
    try {
      const response = await loginUser(userData);
      if (response.error) {
        setError(response.error);
      }
      else {
        localStorage.setItem("token", response.token);
        localStorage.setItem("name", response.name);
        localStorage.setItem("role", response.role);
        localStorage.setItem("id", response.id);
        setSuccess('Logged in successfully!');
        setUserData({ email: '', password: '' });
        setTimeout(() => navigate('/'), 500);
      }
    } catch (err) {
      setError(`${err.response?.data?.message || 'Error occurred'}`);
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <div className="login-square">
        {success && <div className="login-success-message">{success}</div>}
        {error && <div className="login-error-message">{error}</div>}
        {emailError && <div className="login-error-message">{emailError}</div>}

        <form className="login-form" onSubmit={handleSubmit}>
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

          <button type="submit" className="login-submit">Login</button>
        </form>
        <Link to="/" className="back-to-home-btn">
          <i className="fa fa-arrow-left back-icon"></i>
        </Link>
      </div>
    </div>
  );
};

export default LoginComponent;
