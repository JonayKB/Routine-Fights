import React from "react";
import { Dimensions, View } from "react-native";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { borderColor, cardBgColor } from "../utils/Utils";
import Skeleton from "./Skeleton";

const CARD_MARGIN = 20;

/**
 * Placeholder with the exact frame of Post: same card, header, 4:5 photo and
 * action row, so nothing jumps when the real posts replace it.
 */
const PostSkeleton = () => {
  const { darkmode } = useSettingsContext();
  const photoWidth = Dimensions.get("window").width - CARD_MARGIN * 2 - 2;

  return (
    <View
      accessibilityElementsHidden
      importantForAccessibility="no-hide-descendants"
      className={`mx-5 mb-5 rounded-2xl border overflow-hidden ${cardBgColor(darkmode)} ${borderColor(darkmode)}`}
    >
      <View className="flex-row items-center px-4 py-3">
        <Skeleton width={36} height={36} radius={18} />
        <View className="flex-1 ml-3">
          <Skeleton width="38%" height={12} radius={6} />
          <Skeleton width="24%" height={10} radius={5} style={{ marginTop: 6 }} />
        </View>
        <Skeleton width={28} height={14} radius={7} />
      </View>

      <Skeleton width={photoWidth} height={Math.round(photoWidth * 1.25)} radius={0} />

      <View className="flex-row items-center px-5 py-4">
        <Skeleton width={44} height={18} radius={9} />
        <Skeleton width={44} height={18} radius={9} style={{ marginLeft: 16 }} />
      </View>
    </View>
  );
};

export default PostSkeleton;
