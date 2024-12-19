import axios from "axios";

const REST_API_URL = 'http://localhost:8080/api/employees';
const REST_API_GET_URL = 'http://localhost:8080/api/employees/getEmployees';
const REST_API_GET_BY_ID_URL = 'http://localhost:8080/api/employees/getEmployee';
const REST_API_ADD_URL = 'http://localhost:8080/api/employees/addEmployee';
const REST_API_LOGIN_URL = 'http://localhost:8080/api/employees/loginEmployee';
const REST_API_EDIT_URL = 'http://localhost:8080/api/employees/editEmployee';
const REST_API_DELETE_URL = 'http://localhost:8080/api/employees/deleteEmployeeById';

export const listEmployees = () => axios.get(REST_API_URL);

export const getEmployees = async () => {
  try {
    const response = await axios.get(REST_API_GET_URL);
    return response.data;
  } catch (error) {
    console.error("Error while getting all the employees:", error);
    throw error;
  }
};

export const getEmployeeById = async (id) => {
  try {
    const response = await axios.get(REST_API_GET_BY_ID_URL, { params: { id } });
    return response.data;
  } catch (error) {
    console.error("Error while getting an employee:", error);
    throw error;
  }
};

export const addEmployee = async (employeeDTO) => {
  try {
    const response = await axios.post(REST_API_ADD_URL, employeeDTO);
    return response.data;
  } catch (error) {
    console.error("Error while adding an employee:", error);
    throw error;
  }
};

export const loginEmployee = async (loginDTO) => {
  try {
    const response = await axios.post(REST_API_LOGIN_URL, loginDTO);
    return response.data;
  } catch (error) {
    if (error.response.status === 401) {
      return { error: "Invalid email or password" };
    } else {
      console.error("Error while a user logs in:", error);
      throw error;
    }
  }
}

export const editEmployee = async (id, updatedData) => {
  try {
    const response = await axios.put(`${REST_API_EDIT_URL}/${id}`, updatedData);
    return response.data;
  } catch (error) {
    console.error("Error while updating an employee:", error);
    throw error;
  }
};

export const deleteEmployeeById = async (id) => {
  try {
    await axios.delete(REST_API_DELETE_URL, { params: { id } });
    console.log(`Employee with id ${id} deleted successfully!`);
  } catch (error) {
    console.error("Error while deleting an employee:", error);
    throw error;
  }
};