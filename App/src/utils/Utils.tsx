export const uri: string = "http://64.226.71.234:8080";
export const neo4jUri: string = uri+"/graphql";

export const resetNavigation = (navigation: any, path: string) => {
    navigation.navigate(path);
    navigation.reset({
      index: 0,
      routes: [{ name: path }],
    });
}

const numberToString = (number: number) => {
  return new Intl.NumberFormat("en-EN").format(number);
};

export const convertQuantityToString = (quantity: number) => {
  if (quantity.toString().length > 5) {
    if (quantity > 999999) {
      return numberToString(quantity).slice(0, 5) + "M";
    }
    return numberToString(quantity).slice(0, 5) + "K";
  } else {
    return numberToString(quantity);
  }
};