import axios from "axios";
import { CommonData } from "../utils/CommonData";
import { Dispatch } from "react";

export class AuthRepository {
  static async login(email: string, password: string) {
    if (email && password) {
      try {
        const response = await axios.post(CommonData.BASE_URL + "graphql", {
          query: `
            mutation Login($email: String!, $password: String!) {
              login(email: $email, password: $password)
            }`,
          variables: { email, password },
        });
        if (response.data.errors?.length) {
          throw new Error(response.data.errors[0].message);
        }
        const token: string = response.data.data.login;
        localStorage.setItem("token", token);
        return token;
      } catch (error) {
        if (axios.isAxiosError(error)) {
          throw new Error(error.response?.data?.error ?? error.message);
        }
        if (error instanceof Error) {
          throw error;
        }
        throw new Error("Login failed");
      }
    }
  }
  static async logout(state: Dispatch<React.SetStateAction<string>>) {
    localStorage.removeItem("token");
    state("");
  }
}
