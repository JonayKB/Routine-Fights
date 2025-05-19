export const uri: string = "http://64.226.71.234:8080";
export const neo4jUri: string = uri + "/graphql";
export const limit: number = 10;

export const resetNavigation = (navigation: any, path: string) => {
  navigation.navigate(path);
  navigation.reset({
    index: 0,
    routes: [{ name: path }],
  });
};

const numberToString = (number: number) => {
  return new Intl.NumberFormat("en-EN").format(number);
};

export const convertQuantityToString = (quantity: number) => {
  if (quantity?.toString().length > 5) {
    if (quantity > 999999) {
      return numberToString(quantity).slice(0, 5) + "M";
    }
    return numberToString(quantity).slice(0, 5) + "K";
  } else {
    return numberToString(quantity);
  }
};

export const languages = [
  { label: "English", value: "en-EN" },
  { label: "Spanish", value: "es-ES" },
];

export const borderColor = (darkmode: boolean) => darkmode ? "border-[#B28DFF]" : "border-[#7D3C98]";
export const bgColor = (darkmode: boolean) => darkmode ? "bg-[#1C1C1E]" : "bg-[#FCFCFC]";
export const cardBgColor = (darkmode: boolean) => darkmode ? "bg-[#4B294F]" : "bg-[#E8E2F0]";
export const iconColor = (darkmode: boolean) => darkmode ? "#B28DFF" : "#7D3C98";
export const textColor = (darkmode: boolean) => darkmode ? "text-white" : "text-[#1C1C1E]";
