import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { validateToken } from '../services/TokenService';
import '../assets/styles/Homepage.css';

const Homepage = () => {

    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [name, setUserName] = useState(null);
    const [role, setRole] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        const name = localStorage.getItem("name");
        const role = localStorage.getItem("role");
        if (token) {
            validateToken(token)
                .then((data) => {
                    if (data === "Token is valid!") {
                        setIsLoggedIn(true);
                        setUserName(name);
                        setRole(role);
                    } else {
                        setIsLoggedIn(false);
                        setUserName(null);
                        setRole(null);
                        localStorage.removeItem("token");
                        localStorage.removeItem("name");
                        localStorage.removeItem("role");
                        localStorage.removeItem("id");
                    }
                })
        } else {
            setIsLoggedIn(false);
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("name");
        localStorage.removeItem("role");
        localStorage.removeItem("id");
        setIsLoggedIn(false);
        setUserName(null);
        setRole(null);
    };

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="app-name">Appointment Management System</div>
                <nav>
                    {isLoggedIn ? (
                        <div className="logged-in-info">
                            <span className="logged-in-text">Logged in as <strong>{name.split(' ')[0].charAt(0).toUpperCase() + name.split(' ')[0].slice(1).toLowerCase()
                                + " " + name.split(' ')[1].charAt(0).toUpperCase() + name.split(' ')[1].slice(1).toLowerCase()}</strong></span>
                            {role === "USER" && (
                                <Link to="/changeData" className="header-btn">Edit my Data</Link>
                            )}
                            <button onClick={handleLogout} className="header-btn">Log Out</button>
                        </div>
                    ) : (
                        <div className="logged-in-info">
                            <>
                                <Link to="/login" className="header-btn">Login</Link>
                                <Link to="/signup" className="header-btn">Sign Up</Link>
                                <Link to="/employeeLogin" className="header-btn">Login as Employee</Link>
                            </>
                        </div>
                    )}
                </nav>
            </header>
            <main className="home-main">
                <h1>{isLoggedIn ?
                    `Hello ${name.split(' ')[0].charAt(0).toUpperCase() + name.split(' ')[0].slice(1).toLowerCase()}! 
                    Welcome to Your Appointment Management System!` :
                    "Welcome to Your Appointment Management System!"}
                </h1>
                <p>Book appointments, explore employees, and manage your time effortlessly!</p>

                <div className="home-actions">
                    {!isLoggedIn || role === "USER" ? (
                        <Link to="/bookAppointment" className="action-btn">Book an Appointment</Link>
                    ) : null}
                    <Link to="/employees" className="action-btn">Explore Employees</Link>
                    {isLoggedIn && (
                        <Link to="/myAppointments" className="action-btn">My Appointments</Link>
                    )}
                </div>
            </main>
        </div>
    );
};

export default Homepage;
