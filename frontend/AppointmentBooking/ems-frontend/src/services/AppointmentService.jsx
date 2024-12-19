import axios from "axios";

const REST_API_URL = 'http://localhost:8080/api/appointments';
const REST_API_GET_URL = 'http://localhost:8080/api/appointments/getAppointments';
const REST_API_GET_BY_ID_URL = 'http://localhost:8080/api/appointments/getAppointment';
const REST_API_GET_BY_USER_ID_URL = 'http://localhost:8080/api/appointments/getAppointmentsByUserId';
const REST_API_GET_BY_EMPLOYEE_ID_URL = 'http://localhost:8080/api/appointments/getAppointmentsByEmId';
const REST_API_ADD_URL = 'http://localhost:8080/api/appointments/addAppointment';
const REST_API_EDIT_URL = 'http://localhost:8080/api/appointments/editAppointment';
const REST_API_DELETE_URL = 'http://localhost:8080/api/appointments/deleteAppointmentById';
const REST_API_GET_TIMESLOTS_URL = 'http://localhost:8080/api/appointments/getTimeslots';

export const listAppointments = () => axios.get(REST_API_URL);

export const getAppointments = async () => {
  try {
    const response = await axios.get(REST_API_GET_URL);
    return response.data;
  } catch (error) {
    console.error("Error while getting all the appointments:", error);
    throw error;
  }
};

export const getTimeslots = async (date) => {
  try {
    const response = await axios.get(REST_API_GET_TIMESLOTS_URL, { params: { date } });
    return response.data;
  } catch (error) {
    console.error("Error while getting all the timeslots:", error);
    throw error;
  }
};

export const getAppointmentById = async (id) => {
  try {
    const response = await axios.get(REST_API_GET_BY_ID_URL, { params: { id } });
    return response.data;
  } catch (error) {
    console.error("Error while getting an appointment:", error);
    throw error;
  }
};

export const getAppointmentsByUserId = async (userid) => {
  try {
    const response = await axios.get(`${REST_API_GET_BY_USER_ID_URL}/${userid}`);
    return response.data;
  } catch (error) {
    console.error("Error while getting appointments by user id:", error);
    throw error;
  }
}

export const getAppointmentsByEmployeeId = async (id) => {
  try {
    const response = await axios.get(`${REST_API_GET_BY_EMPLOYEE_ID_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error while getting appointments by employee id:", error);
    throw error;
  }
}

export const addAppointment = async (appointmentDTO) => {
  try {
    const response = await axios.post(REST_API_ADD_URL, appointmentDTO);
    return response.data;
  } catch (error) {
    console.error("Error while adding an appointment:", error);
    throw error;
  }
};

export const editAppointment = async (id, updatedData) => {
  try {
    const response = await axios.put(`${REST_API_EDIT_URL}/${id}`, updatedData);
    return response.data;
  } catch (error) {
    console.error("Error while updating an appointment:", error);
    throw error;
  }
};

export const deleteAppointmentById = async (id) => {
  try {
    await axios.delete(`${REST_API_DELETE_URL}/${id}`);
    console.log(`Appointment with id ${id} deleted successfully!`);
  } catch (error) {
    console.error("Error while deleting an appointment:", error);
    throw error;
  }
};