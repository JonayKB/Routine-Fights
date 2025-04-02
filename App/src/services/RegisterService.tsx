import axios from "axios";
import { UserIn } from "../utils/User";
import { uri } from "../utils/Utils";

export const register = async (userIn: UserIn): Promise<number> => {
  try {
    return (await axios.post(uri+"/auth/register", userIn)).status;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};
