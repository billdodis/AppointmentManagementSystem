import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getEmployees } from '../services/EmployeeService';
import placeholderImage from '../assets/images/default-profile-picture-placeholder.png';
import vasileios_dodis from '../assets/images/vasileios_dodis.jpg';
import '../assets/styles/ExploreEmployees.css';
import '../assets/styles/MainHeader.css';
import '../assets/styles/BackToHomeButton.css';
import 'font-awesome/css/font-awesome.min.css';


const ExploreEmployeesComponent = () => {
  const [employees, setEmployees] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getEmployees();
        setEmployees(data);
      } catch (error) {
        console.error('Error fetching employees:', error);
      }
    };

    fetchData();
  }, []);

  const images = {
    "vasileios_dodis": vasileios_dodis,
  };
  
  const getEmployeeImage = (firstName, lastName) => {
    const imageKey = `${firstName.toLowerCase()}_${lastName.toLowerCase()}`;
    return images[imageKey] || placeholderImage;
  };

  return (
    <div className="explore-employees-container">
      <h2>Our Employees</h2>
      <div className="employees-grid">
        {employees.map((employee) => (
          <div key={employee.emId} className="employee-card">
            <img
              src={getEmployeeImage(employee.firstName, employee.lastName)}
              alt={`${employee.firstName} ${employee.lastName}`}
            />
            <p><strong>First Name:</strong> {employee.firstName}</p>
            <p><strong>Last Name:</strong> {employee.lastName}</p>
          </div>
        ))}
      </div>
      <Link to="/" className="back-to-home-btn">
        <i className="fa fa-arrow-left back-icon"></i>
      </Link>
    </div>
  );
};

export default ExploreEmployeesComponent;