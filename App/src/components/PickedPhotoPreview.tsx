import React, { useState } from "react";
import { LayoutChangeEvent, Text, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { translations } from "../../translations/translation";
import { useImageContext } from "../contexts/ImageContextProvider";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import CroppedImage from "./CroppedImage";
import PhotoCropper from "./PhotoCropper";

/**
 * The photo picked in the image context, cropped, with buttons to adjust the
 * crop again or pick another one. Fills its parent.
 */
const PickedPhotoPreview = () => {
  const { language } = useSettingsContext();
  const { picked, setPicked, clear, width, height } = useImageContext();
  const tr = translations[language || "en-EN"].screens;
  const [box, setBox] = useState<{ w: number; h: number } | null>(null);
  const [editing, setEditing] = useState(false);

  if (!picked) return null;
  const circle = width === height;

  const onLayout = (e: LayoutChangeEvent) =>
    setBox({ w: e.nativeEvent.layout.width, h: e.nativeEvent.layout.height });

  const Button = ({ icon, label, onPress }: { icon: string; label: string; onPress: () => void }) => (
    <TouchableOpacity
      onPress={onPress}
      activeOpacity={0.8}
      className="flex-row items-center h-10 px-4 mx-1 rounded-full bg-[#12110FCC] border border-[#EFE9DE1A]"
    >
      <Icon name={icon} size={16} color="#EFE9DE" />
      <Text className="ml-2 text-sm font-medium text-[#EFE9DE]">{label}</Text>
    </TouchableOpacity>
  );

  return (
    <View className="flex-1 bg-[#12110F]">
      <View className="flex-1 items-center justify-center" style={{ padding: 16, paddingBottom: 68 }}>
        <View className="flex-1 self-stretch items-center justify-center" onLayout={onLayout}>
          {box && (
            <CroppedImage
              picked={picked}
              maxWidth={box.w}
              maxHeight={box.h}
              borderRadius={circle ? Math.min(box.w, box.h) : 12}
            />
          )}
        </View>
      </View>
      <View className="absolute bottom-4 left-0 right-0 flex-row justify-center">
        <Button icon="crop-outline" label={tr.ImageEditor.edit} onPress={() => setEditing(true)} />
        <Button icon="refresh" label={tr.UploadForm.changePicture} onPress={clear} />
      </View>

      <PhotoCropper
        source={editing ? { uri: picked.uri, width: picked.width, height: picked.height } : null}
        aspect={width / height}
        circle={circle}
        onCancel={() => setEditing(false)}
        onDone={({ crop }) => {
          setPicked({ ...picked, crop, aspect: width / height });
          setEditing(false);
        }}
      />
    </View>
  );
};

export default PickedPhotoPreview;
