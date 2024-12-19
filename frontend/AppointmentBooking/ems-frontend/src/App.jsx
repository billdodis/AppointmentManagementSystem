import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css'
import SignUpComponent from './components/SignUpComponent';
import Homepage from './components/Homepage';
import LoginComponent from './components/LoginComponent';
import ExploreEmployeesComponent from './components/ExploreEmployeesComponent';
import MakeAppointmentComponent from './components/MakeAppointmentComponent';
import EmployeeLoginComponent from './components/EmployeeLoginComponent';
import MyAppointmentsComponent from './components/MyAppointmentsComponent';
import ChangeDataComponent from './components/ChangeDataComponent';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/signup" element={<SignUpComponent />} />
        <Route path="/login" element={<LoginComponent />} />
        <Route path="/employeeLogin" element={<EmployeeLoginComponent />} />
        <Route path="changeData" element={<ChangeDataComponent />} />
        <Route path="/employees" element={<ExploreEmployeesComponent />} />
        <Route path="/bookAppointment" element={<MakeAppointmentComponent />} />
        <Route path="/myAppointments" element={<MyAppointmentsComponent />} />
      </Routes>
    </Router>
  )
}

export default App
