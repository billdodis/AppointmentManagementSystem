import axios from "axios";

const REST_API_VALIDATE_URL = 'http://localhost:8080/api/token/validateToken';

export const validateToken = async (token) => {
    try {
        const response = await axios.post(REST_API_VALIDATE_URL, {}, {
            headers: {
                "Authorization": `Bearer ${token}`,
            }
        });
        return response.data;
    } catch (error) {
        console.error("Error validating token:", error);
        return false;
    }
};