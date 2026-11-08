// google-libphonenumber ships without types; it comes with react-native-phone-number-input.
// eslint-disable-next-line @typescript-eslint/no-var-requires
const { PhoneNumberUtil } = require("google-libphonenumber");

const util = PhoneNumberUtil.getInstance();

/**
 * Splits a stored number such as "+34612345678" into its country ("ES") and
 * national part ("612345678"), so the phone input can show them separately.
 */
export const splitPhoneNumber = (
  full?: string | null
): { country?: string; national: string } => {
  if (!full) return { national: "" };
  try {
    const parsed = util.parse(full);
    return {
      country: util.getRegionCodeForNumber(parsed) || undefined,
      national: String(parsed.getNationalNumber() ?? ""),
    };
  } catch {
    return { national: full.replace(/^\+\d{1,3}/, "") };
  }
};
