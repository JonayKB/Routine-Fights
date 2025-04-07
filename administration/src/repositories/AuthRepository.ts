import axios from "axios";
import { CommonData } from "../utils/CommonData";

export class AuthRepository{
    static async login(email: string, password: string) {
        if(email && password){
            try {
                const response = await axios.post(CommonData.BASE_URL + 'auth/login?email='+email+'&password='+password)
                if(response.status === 200){
                    localStorage.setItem('token', response.data);
                    return response.data;
                }
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    throw new Error(error.response?.data);
                }
                throw new Error("Login failed");
                
            }
            
        }
    }
}