import axios from "axios";
import RNSecureKeyStore from "react-native-secure-key-store";
import { uri } from "../utils/Utils";
import { PickedImage } from "../utils/images";

/**
 * Uploads a picked photo. The crop and rotation chosen in the editor travel as
 * form fields and are applied by the API, which also stores every size.
 *
 * @returns the stored image name.
 */
export const uploadImage = async (picked: PickedImage): Promise<string> => {
  const token = await RNSecureKeyStore.get("token");
  const formData = new FormData();
  formData.append("file", {
    uri: picked.uri.includes("://") ? picked.uri : "file://" + picked.uri,
    name: picked.fileName,
    type: picked.type || "image/jpeg",
  } as any);
  const { crop } = picked;
  formData.append("cropX", String(crop.x));
  formData.append("cropY", String(crop.y));
  formData.append("cropWidth", String(crop.width));
  formData.append("cropHeight", String(crop.height));
  formData.append("rotation", String(crop.rotation));

  try {
    const response = await axios.post(uri + "/images/", formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "multipart/form-data",
      },
      timeout: 60_000,
    });
    return response.data;
  } catch (error) {
    // nginx answers 413 with an HTML page: replace it with the API's message.
    if (error?.response?.status === 413) {
      throw new Error("Image is too large");
    }
    console.error("Error uploading image:", error?.response?.data ?? error?.message);
    throw error;
  }
};
