import axios from "axios";

const REST_API_URL = 'http://localhost:8080/api/users';
const REST_API_GET_URL = 'http://localhost:8080/api/users/getUsers';
const REST_API_GET_BY_ID_URL = 'http://localhost:8080/api/users/getUser';
const REST_API_ADD_URL = 'http://localhost:8080/api/users/addUser';
const REST_API_LOGIN_URL = 'http://localhost:8080/api/users/loginUser';
const REST_API_EDIT_URL = 'http://localhost:8080/api/users/editUser';
const REST_API_DELETE_URL = 'http://localhost:8080/api/users/deleteUserById';

export const listUsers = () => axios.get(REST_API_URL);

export const getUsers = async () => {
  try {
    const response = await axios.get(REST_API_GET_URL);
    return response.data;
  } catch (error) {
    console.error("Error while getting all the users:", error);
    throw error;
  }
};

export const getUserById = async (id) => {
  try {
    const response = await axios.get(REST_API_GET_BY_ID_URL, { params: { id } });
    return response.data;
  } catch (error) {
    console.error("Error while getting a user:", error);
    throw error;
  }
};

export const addUser = async (userInpDTO) => {
  try {
    const response = await axios.post(REST_API_ADD_URL, userInpDTO);
    return response.data;
  } catch (error) {
    if (error.response.status === 400 && error.response.data === "Email already exists!") {
      return { error: "Email already exists!" };
    } else {
      console.error("Error while adding a user:", error);
      throw error;
    }
  }
};

export const loginUser = async (loginDTO) => {
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

export const editUser = async (id, userInpDTO) => {
  try {
    const response = await axios.put(`${REST_API_EDIT_URL}/${id}`, userInpDTO);
    return response.data;
  } catch (error) {
    if (error.response.status === 400) {
      return { error: "Wrong old password!" };
    } else {
      console.error("Error while updating a user", error);
      throw error;
    }
  }
};

export const deleteUserById = async (id) => {
  try {
    await axios.delete(REST_API_DELETE_URL, { params: { id } });
    console.log(`User with id ${id} deleted successfully!`);
  } catch (error) {
    console.error("Error while deleting a user:", error);
    throw error;
  }
};