import React from "react";
import { Modal, Pressable, ScrollView, Text, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { colors, palette } from "../styles/theme";

export type SheetOption = {
  value: string;
  label: string;
  /** Second line in smaller, muted text. */
  description?: string;
  icon?: string;
};

type Props = {
  visible: boolean;
  title: string;
  options: SheetOption[];
  selected: string;
  onSelect: (value: string) => void;
  onClose: () => void;
};

/** Bottom sheet with a single-choice list, styled like the confirm sheet. */
const OptionSheet = ({ visible, title, options, selected, onSelect, onClose }: Props) => {
  const { darkmode } = useSettingsContext();
  const insets = useSafeAreaInsets();
  const c = colors(darkmode);
  const accent = darkmode ? palette.accentOnDark : palette.accent;
  // Keep labels aligned when only some options have an icon.
  const withIcons = options.some((o) => o.icon);

  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onClose}>
      <Pressable style={{ flex: 1, backgroundColor: "#00000080" }} onPress={onClose} />
      <View
        style={{
          maxHeight: "75%",
          backgroundColor: c.card,
          borderTopLeftRadius: 20,
          borderTopRightRadius: 20,
          paddingTop: 12,
          paddingBottom: Math.max(insets.bottom, 16),
        }}
      >
        <View style={{ alignSelf: "center", width: 36, height: 4, borderRadius: 2, backgroundColor: c.raised }} />
        <Text style={{ marginTop: 16, marginBottom: 8, paddingHorizontal: 20, fontSize: 18, fontWeight: "600", color: c.text }}>
          {title}
        </Text>
        <ScrollView accessibilityRole="radiogroup">
          {options.map((option) => {
            const isSelected = option.value === selected;
            return (
              <TouchableOpacity
                key={option.value}
                accessibilityRole="radio"
                accessibilityState={{ selected: isSelected }}
                activeOpacity={0.6}
                onPress={() => {
                  onSelect(option.value);
                  onClose();
                }}
                style={{
                  flexDirection: "row",
                  alignItems: "center",
                  minHeight: 60,
                  paddingHorizontal: 20,
                  paddingVertical: 10,
                  backgroundColor: isSelected ? c.raised : "transparent",
                }}
              >
                {withIcons && (
                  <View style={{ width: 34 }}>
                    {!!option.icon && <Icon name={option.icon} size={20} color={c.textSecondary} />}
                  </View>
                )}
                <View style={{ flex: 1 }}>
                  <Text style={{ fontSize: 16, color: c.text, fontWeight: isSelected ? "600" : "400" }}>
                    {option.label}
                  </Text>
                  {!!option.description && (
                    <Text style={{ marginTop: 2, fontSize: 13, color: c.textMuted }}>{option.description}</Text>
                  )}
                </View>
                {isSelected && <Icon name="checkmark" size={22} color={accent} />}
              </TouchableOpacity>
            );
          })}
        </ScrollView>
      </View>
    </Modal>
  );
};

export default OptionSheet;
