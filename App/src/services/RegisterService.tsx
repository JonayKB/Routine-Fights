import axios from "axios";
import { UserIn } from "../utils/User";

export const register = async (uri: string, userIn: UserIn) => {
  try {
    return (await axios.post(uri, userIn)).status;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};
