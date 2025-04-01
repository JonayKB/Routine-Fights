import RNSecureKeyStore from 'react-native-secure-key-store';

export const logout = async () => {
    await RNSecureKeyStore.remove("token");
}