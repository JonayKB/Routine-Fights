import axios from "axios";
import { CommonData } from "../utils/CommonData";

export class ImageRepository {
  static async getImage(imageId: string, token: string) {
    const response = await axios.get(
      CommonData.BASE_URL + "images/" + imageId,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        responseType: "blob",
      }
    );
    return URL.createObjectURL(response.data);
  }
  static async uploadImage(token: string, file: File) {
    const formData = new FormData();
    formData.append("file", file, file.name);

    try {
      const response = await axios.post(
        CommonData.BASE_URL + "images/",
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error uploading image:", error);
      throw new Error("Image upload failed");
    }
  }
}
