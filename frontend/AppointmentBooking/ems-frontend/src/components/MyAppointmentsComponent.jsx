import React, { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import { getAppointmentsByUserId, getAppointmentsByEmployeeId, deleteAppointmentById } from '../services/AppointmentService';
import { validateToken } from "../services/TokenService";
import "../assets/styles/MyAppointments.css";
import '../assets/styles/BackToHomeButton.css';
import '../assets/styles/Popup.css';
import '../assets/styles/AppointmentSquare.css';
import 'font-awesome/css/font-awesome.min.css';

const MyAppointmentsComponent = () => {
    const [appointments, setAppointments] = useState([]);
    const [role, setRole] = useState(null);
    const [error, setError] = useState(null);
    const [timedOut, setTimedOut] = useState(null);
    const [cancelPopupData, setCancelPopupData] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            validateToken(token)
                .then((data) => {
                    if (data === "Token is valid!") {
                        fetchAppointments();
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

    const fetchAppointments = async () => {
        const role = localStorage.getItem("role");
        setRole(role);
        const id = localStorage.getItem("id");
        try {
            let appointmentsData = [];
            if (role === "USER") {
                appointmentsData = await getAppointmentsByUserId(id);
            } else if (role === "EMPLOYEE") {
                appointmentsData = await getAppointmentsByEmployeeId(id);
            }
            appointmentsData.sort((a, b) => new Date(a.date) - new Date(b.date));
            setAppointments(appointmentsData);
        } catch (error) {
            console.error("Error fetching appointments:", error);
            setError("Failed to fetch appointments. Please try again.");
        }
    };

    const handleCancelClick = (appointment) => {
        setCancelPopupData(appointment);
    };

    const handleCancelAppointment = async () => {
        if (!cancelPopupData) return;
        const id = cancelPopupData.apId;
        try {
            await deleteAppointmentById(id);
            setCancelPopupData(null);
            fetchAppointments();
        } catch (error) {
            setError("Failed to cancel the appointment. Please try again.");
        }
    };

    return (
        <div className="my-appointments-container">
            <h2>My Appointments</h2>
            <div className="appointments-list">
                {appointments.length > 0 ? (
                    appointments.map((appointment, index) => (
                        <div key={index}
                            className={`appointment-square ${role === 'EMPLOYEE' ? 'employee-square' : 'user-square'}`}>
                            <h4>{new Intl.DateTimeFormat("en-US", {
                                weekday: "long",
                                year: "numeric",
                                month: "long",
                                day: "numeric"
                            }).format(new Date(appointment.date))}
                                <br />@ {appointment.date.slice(11, 16)}
                            </h4>
                            {role === "USER" && (
                                <><p>Booked with Employee: {appointment.employee?.firstName} {appointment.employee?.lastName}</p></>
                            )}
                            {role === "EMPLOYEE" && (
                                <><p>Booked by User: {appointment.user?.firstName} {appointment.user?.lastName}</p></>
                            )}
                            <button className="cancel-btn" onClick={() => handleCancelClick(appointment)}>
                                Cancel Appointment!
                            </button>
                        </div>
                    ))
                ) : (
                    <p>You have no upcoming appointments.</p>
                )}
            </div>
            {timedOut && (
                <div className="popup">
                    <div className="popup-content">
                        <p><strong>Your session has expired!</strong><br />Please log in again, to view or manage your appointments.</p>
                        <Link to="/login" className="confirm-btn">Login</Link>
                    </div>
                </div>
            )}
            {error && <p className="error-message">{error}</p>}
            {cancelPopupData && (
                <div className="popup">
                    <div className="popup-content">
                        <p>Are you sure you want to <strong>cancel</strong> the appointment
                            on:<br /><strong>{new Intl.DateTimeFormat("en-US", {
                                weekday: "long",
                                year: "numeric",
                                month: "long",
                                day: "numeric"
                            }).format(new Date(cancelPopupData.date))}
                            </strong> <br /> <strong>@ {cancelPopupData.date.slice(11, 16)}</strong>?</p>
                        <div className="popup-buttons">
                            <button className="confirm-btn" onClick={() => handleCancelAppointment(cancelPopupData.id)}>Yes!
                            </button>
                            <button className="cancel-btn" onClick={() => setCancelPopupData(null)}>No!
                            </button>
                        </div>
                    </div>
                </div>
            )}
            <Link to="/" className="back-to-home-btn">
                <i className="fa fa-arrow-left back-icon"></i>
            </Link>
        </div>
    );
};

export default MyAppointmentsComponent;
