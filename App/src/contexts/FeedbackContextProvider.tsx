import React, { createContext, useCallback, useContext, useEffect, useRef, useState } from "react";
import { Animated, Easing, Modal, Pressable, Text, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "./SettingsContextProvider";
import { colors, palette } from "../styles/theme";

/**
 * In-app feedback without system alerts: a toast at the bottom of the screen
 * for results and errors, and a bottom sheet for confirmations.
 */

type ToastKind = "error" | "success" | "info";
type Toast = { id: number; message: string; kind: ToastKind };

type ConfirmOptions = {
  title: string;
  message?: string;
  confirmLabel: string;
  cancelLabel: string;
  destructive?: boolean;
};

type FeedbackContextType = {
  error: (message: string) => void;
  success: (message: string) => void;
  info: (message: string) => void;
  confirm: (options: ConfirmOptions) => Promise<boolean>;
};

const FeedbackContext = createContext<FeedbackContextType>({
  error: () => {},
  success: () => {},
  info: () => {},
  confirm: async () => false,
});

export const useToast = () => useContext(FeedbackContext);

const TOAST_MS = 3500;
/** Keeps the toast above the bottom tab bar. */
const BOTTOM_OFFSET = 84;

const FeedbackContextProvider = ({ children }: { children: React.ReactNode }) => {
  const { darkmode } = useSettingsContext();
  const [toast, setToast] = useState<Toast | null>(null);
  const [pending, setPending] = useState<(ConfirmOptions & { resolve: (v: boolean) => void }) | null>(null);
  const appear = useRef(new Animated.Value(0)).current;
  const timer = useRef<ReturnType<typeof setTimeout> | null>(null);
  const seq = useRef(0);

  const hide = useCallback(() => {
    Animated.timing(appear, { toValue: 0, duration: 180, useNativeDriver: true }).start(() => setToast(null));
  }, [appear]);

  const show = useCallback(
    (message: string, kind: ToastKind) => {
      if (!message) return;
      if (timer.current) clearTimeout(timer.current);
      seq.current += 1;
      setToast({ id: seq.current, message, kind });
      appear.setValue(0);
      Animated.timing(appear, {
        toValue: 1,
        duration: 220,
        easing: Easing.out(Easing.cubic),
        useNativeDriver: true,
      }).start();
      timer.current = setTimeout(hide, TOAST_MS);
    },
    [appear, hide]
  );

  useEffect(() => () => {
    if (timer.current) clearTimeout(timer.current);
  }, []);

  const confirm = useCallback(
    (options: ConfirmOptions) => new Promise<boolean>((resolve) => setPending({ ...options, resolve })),
    []
  );

  const closeConfirm = (value: boolean) => {
    pending?.resolve(value);
    setPending(null);
  };

  const c = colors(darkmode);
  const accent = toast?.kind === "error" ? palette.like : toast?.kind === "success" ? "#16A34A" : c.textSecondary;
  const icon = toast?.kind === "error" ? "alert-circle" : toast?.kind === "success" ? "checkmark-circle" : "information-circle";

  return (
    <FeedbackContext.Provider
      value={{
        error: (m) => show(m, "error"),
        success: (m) => show(m, "success"),
        info: (m) => show(m, "info"),
        confirm,
      }}
    >
      {children}

      {toast && (
        <Animated.View
          pointerEvents="box-none"
          style={{
            position: "absolute",
            left: 16,
            right: 16,
            bottom: BOTTOM_OFFSET,
            opacity: appear,
            transform: [{ translateY: appear.interpolate({ inputRange: [0, 1], outputRange: [12, 0] }) }],
          }}
        >
          <Pressable
            onPress={hide}
            accessibilityRole="alert"
            style={{
              flexDirection: "row",
              alignItems: "center",
              paddingHorizontal: 16,
              paddingVertical: 12,
              borderRadius: 14,
              backgroundColor: darkmode ? "#262421" : "#1F1B16",
              borderWidth: 1,
              borderColor: darkmode ? "#EFE9DE1A" : "#1F1B16",
            }}
          >
            <Icon name={icon} size={18} color={accent} />
            <Text style={{ flex: 1, marginLeft: 10, color: "#EFE9DE", fontSize: 14, lineHeight: 20 }}>
              {toast.message}
            </Text>
          </Pressable>
        </Animated.View>
      )}

      <Modal visible={!!pending} transparent animationType="fade" onRequestClose={() => closeConfirm(false)}>
        <Pressable style={{ flex: 1, backgroundColor: "#00000080" }} onPress={() => closeConfirm(false)} />
        <View
          style={{
            backgroundColor: c.card,
            borderTopLeftRadius: 20,
            borderTopRightRadius: 20,
            paddingHorizontal: 20,
            paddingTop: 12,
            paddingBottom: 24,
          }}
        >
          <View style={{ alignSelf: "center", width: 36, height: 4, borderRadius: 2, backgroundColor: c.raised }} />
          <Text style={{ marginTop: 20, fontSize: 18, fontWeight: "600", color: c.text }}>{pending?.title}</Text>
          {!!pending?.message && (
            <Text style={{ marginTop: 6, fontSize: 14, lineHeight: 20, color: c.textSecondary }}>
              {pending.message}
            </Text>
          )}
          <View style={{ flexDirection: "row", marginTop: 24 }}>
            <TouchableOpacity
              activeOpacity={0.7}
              onPress={() => closeConfirm(false)}
              style={{
                flex: 1,
                height: 48,
                marginRight: 12,
                borderRadius: 12,
                alignItems: "center",
                justifyContent: "center",
                backgroundColor: c.raised,
                borderWidth: 1,
                borderColor: c.hairline,
              }}
            >
              <Text style={{ fontSize: 16, fontWeight: "500", color: c.text }}>{pending?.cancelLabel}</Text>
            </TouchableOpacity>
            <TouchableOpacity
              activeOpacity={0.85}
              onPress={() => closeConfirm(true)}
              style={{
                flex: 1,
                height: 48,
                borderRadius: 12,
                alignItems: "center",
                justifyContent: "center",
                backgroundColor: pending?.destructive ? palette.like : palette.accent,
              }}
            >
              <Text style={{ fontSize: 16, fontWeight: "600", color: "#FFFFFF" }}>{pending?.confirmLabel}</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </FeedbackContext.Provider>
  );
};

export default FeedbackContextProvider;
