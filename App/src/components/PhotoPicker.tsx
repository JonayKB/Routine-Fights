import React, { useState } from "react";
import { Text, TouchableOpacity, View } from "react-native";
import { Asset, ImageLibraryOptions, launchCamera, launchImageLibrary } from "react-native-image-picker";
import Icon from "react-native-vector-icons/Ionicons";
import { translations } from "../../translations/translation";
import { useImageContext } from "../contexts/ImageContextProvider";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useToast } from "../contexts/FeedbackContextProvider";
import { MAX_UPLOAD_BYTES, PICKER_MAX_SIDE } from "../utils/images";
import { borderColor, cardBgColor, iconColor, raisedBgColor, textColor } from "../utils/Utils";
import PhotoCropper, { CropSource } from "./PhotoCropper";

const PICKER_OPTIONS: ImageLibraryOptions = {
  mediaType: "photo",
  // Downscale and recompress on the device: faster uploads, less data.
  maxWidth: PICKER_MAX_SIDE,
  maxHeight: PICKER_MAX_SIDE,
  quality: 0.9,
  selectionLimit: 1,
};

/**
 * Lets the user take or choose a photo, then opens the crop editor with the
 * aspect ratio set in the image context. The result is stored in that context.
 */
const PhotoPicker = () => {
  const { language, darkmode } = useSettingsContext();
  const { setPicked, width, height } = useImageContext();
  const toast = useToast();
  const tr = translations[language || "en-EN"].screens;
  const [asset, setAsset] = useState<Asset | null>(null);

  const pick = (source: "camera" | "library") => {
    const launch = source === "camera" ? launchCamera : launchImageLibrary;
    launch(PICKER_OPTIONS, (response) => {
      if (response.didCancel) return;
      const picked = response.assets?.[0];
      if (response.errorCode || !picked?.uri) {
        toast.error(tr.ImageEditor.loadError);
        return;
      }
      if (picked.fileSize && picked.fileSize > MAX_UPLOAD_BYTES) {
        toast.error(tr.ImageEditor.tooLarge);
        return;
      }
      setAsset(picked);
    });
  };

  const cropSource: CropSource | null = asset?.uri
    ? { uri: asset.uri, width: asset.width, height: asset.height }
    : null;

  const Option = ({ icon, label, source }: { icon: string; label: string; source: "camera" | "library" }) => (
    <TouchableOpacity
      activeOpacity={0.7}
      onPress={() => pick(source)}
      className={`flex-1 items-center justify-center py-6 mx-1.5 rounded-xl border ${raisedBgColor(
        darkmode
      )} ${borderColor(darkmode)}`}
    >
      <Icon name={icon} size={24} color={iconColor(darkmode)} />
      <Text className={`mt-3 text-sm font-medium text-center px-2 ${textColor(darkmode)}`}>{label}</Text>
    </TouchableOpacity>
  );

  return (
    <View className={`flex-1 ${cardBgColor(darkmode)} justify-center px-4`}>
      <View className="flex-row">
        <Option icon="camera-outline" label={tr.UploadForm.takeFromCamera} source="camera" />
        <Option icon="images-outline" label={tr.UploadForm.selectFromLibrary} source="library" />
      </View>

      <PhotoCropper
        source={cropSource}
        aspect={width / height}
        circle={width === height}
        onCancel={() => setAsset(null)}
        onDone={({ crop, width: w, height: h }) => {
          if (!asset?.uri) return;
          setPicked({
            uri: asset.uri,
            type: asset.type ?? "image/jpeg",
            fileName: asset.fileName ?? `photo-${Date.now()}.jpg`,
            width: w,
            height: h,
            crop,
            aspect: width / height,
          });
          setAsset(null);
        }}
      />
    </View>
  );
};

export default PhotoPicker;
