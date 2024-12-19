import React, { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import Calendar from "react-calendar";
import { addAppointment, getTimeslots } from '../services/AppointmentService';
import { validateToken } from '../services/TokenService';
import "react-calendar/dist/Calendar.css";
import '../assets/styles/MainHeader.css';
import "../assets/styles/MakeAppointment.css";
import '../assets/styles/BackToHomeButton.css';
import '../assets/styles/Popup.css';
import '../assets/styles/AppointmentSquare.css';
import 'font-awesome/css/font-awesome.min.css';


const MakeAppointmentComponent = () => {
    const [selectedDate, setSelectedDate] = useState(null);
    const [availableAppointments, setAvailableAppointments] = useState([]);
    const [userId, setUserId] = useState(null);
    const [error, setError] = useState(null);
    const [popupData, setPopupData] = useState(null);
    const [showLoginPopup, setShowLoginPopup] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem("token");
        const id = localStorage.getItem("id");
        if (token) {
            validateToken(token)
                .then((data) => {
                    if (data === "Token is valid!") {
                        setUserId(id);
                        setShowLoginPopup(false);
                    } else {
                        setUserId(null);
                        setShowLoginPopup(true);
                        localStorage.removeItem("token");
                        localStorage.removeItem("name");
                        localStorage.removeItem("role");
                        localStorage.removeItem("id");
                    }
                })
        }
        else {
            setUserId(null);
        }
    }, []);

    const isDateDisabled = ({ date, view }) => {
        if (view === "month") {
            const today = new Date();
            return date < today.setHours(0, 0, 0, 0) || date.getDay() === 0;
        }
        return false;
    };

    const getTileContent = ({ date, view }) => {
        const currentDate = new Date();
        const isPastDay = date < currentDate.setHours(0, 0, 0, 0);
        if (view === "month" && (date.getDay() === 0 || isPastDay)) {
            return <span style={{ color: "red", fontSize: "0.8rem" }}>  X</span>;
        }
        return null;
    };

    const handleDateChange = async (date) => {
        if (!isDateDisabled({ date, view: "month" })) {
            setSelectedDate(date);
            try {
                setError(null);
                const localDateTime = new Date(date);
                localDateTime.setHours(24, 0, 0, 0);
                const formattedDateTime = localDateTime.toISOString().slice(0, 19);
                const availableSlots = await getTimeslots(formattedDateTime);
                setAvailableAppointments(availableSlots);
            } catch (error) {
                console.error("Error fetching available appointments:", error);
                setError("Failed to fetch available appointments. Please try again.");
            }
        }
    };

    const handleAppointmentClick = (time, employee) => {
        if (!userId) {
            setShowLoginPopup(true);
        } else {
            setPopupData({ time, employee });
        }
    };

    const confirmAppointment = async () => {
        if (!popupData) return;
        const { time, employee } = popupData;
        try {
            selectedDate.setHours(24, 0, 0, 0);
            const formattedDate = `${selectedDate.toISOString().slice(0, 10)}T${time}`;
            const appointmentDTO = {
                emId: employee.emId,
                userId: userId,
                date: formattedDate,
            };
            selectedDate.setHours(-24, 0, 0, 0);
            await addAppointment(appointmentDTO);
            setError(null);
            handleDateChange(selectedDate);
        } catch (error) {
            setError(error.response?.data?.message || "Failed to book the appointment. Please try again.");
        }
    };

    return (
        <div className="make-appointment-container">
            <h2>Make an Appointment</h2>
            <div className="calendar-container">
                <Calendar
                    onChange={handleDateChange}
                    value={selectedDate}
                    tileClassName={({ date, view }) =>
                        view === "month" && (date.getDay() === 0 || date < new Date().setHours(0, 0, 0, 0))
                            ? "disabled-date"
                            : ""
                    }
                    tileDisabled={isDateDisabled}
                    tileContent={getTileContent}
                    locale="en-GB"
                />
            </div>
            {selectedDate && (
                <div className="appointments-container">
                    <h3>Available Appointments on {selectedDate.toDateString()}</h3>
                    <div className="appointments-list">
                        {availableAppointments.length > 0 ? (
                            availableAppointments.map((slot, index) => (
                                slot.employeeList.length > 0 && (
                                    <div key={index} className="appointment-square">
                                        <h4>@ {slot.time.slice(0, 5)} <br /> <br /> Book Appointment with: </h4>
                                        <ul className="time-list">
                                            {slot.employeeList.map((employee, empIndex) => (
                                                <li key={empIndex} className="time-item">
                                                    <button
                                                        className="time-button"
                                                        onClick={() => handleAppointmentClick(slot.time, employee)}
                                                    >
                                                        {employee.firstName} {employee.lastName}
                                                    </button>
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                )
                            ))
                        ) : (
                            <p>No appointments available for this date.</p>
                        )}
                    </div>
                </div>
            )}
            {popupData && (
                <div className="popup">
                    <div className="popup-content">
                        <p>
                            Do you want to book an appointment with{" "}
                            <strong>{popupData.employee.firstName} {popupData.employee.lastName}</strong> at{" "}
                            <strong>{popupData.time.slice(0, 5)}</strong>?
                        </p>
                        <div className="popup-buttons">
                            <button className="confirm-btn" onClick={async () => {
                                await confirmAppointment();
                                setTimeout(() => {
                                    setPopupData(null);
                                }, 250);
                            }
                            }>
                                Book it!
                            </button>
                            <button className="cancel-btn" onClick={() => setPopupData(null)}>
                                Cancel!
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {showLoginPopup && (
                <div className="popup">
                    <div className="popup-content">
                        <p>You must be <strong>logged in</strong> to book an appointment!</p>
                        <div className="popup-buttons">
                            <Link to="/login" className="confirm-btn">Login!</Link>
                            <Link to="/signup" className="confirm-btn">Sign Up!</Link>
                            <button onClick={() => setShowLoginPopup(false)} className="cancel-btn">Cancel!</button>
                        </div>
                    </div>
                </div>
            )}
            {error && <p className="error-message">{error}</p>}
            <Link to="/" className="back-to-home-btn">
                <i className="fa fa-arrow-left back-icon"></i>
            </Link>
        </div>
    );
};

export default MakeAppointmentComponent;
