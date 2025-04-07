import React, { FormEvent, useContext, useState } from "react";
import { MainContext } from "../components/MainContext";
import { AuthRepository } from "../repositories/AuthRepository";

type Props = {};

const LoginScreen = (props: Props) => {
   const { setToken } = useContext(MainContext);
   const [error, setError] = useState<string | null>(null);
  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget as HTMLFormElement);
    const email = formData.get("email") as string;
    const password = formData.get("password") as string;
    try {
      const token = await AuthRepository.login(email, password);
      if (token) {
        setToken(token);
        window.location.href = "/";
      } 
      
    } catch (error) {
      if (error instanceof Error) {
        setError(error.message);
      } else {
        setError("An unknown error occurred");
      }
    }
    
  };

  return (
    <div>
    <form onSubmit={handleSubmit}>
      <input type="text" placeholder="Enter your email" name="email" />
      <input type="password" placeholder="Enter your password" name="password" />
      <button type="submit">Login</button>
    </form>
    {error && <p>{error}</p>}
    </div>
  );
};

export default LoginScreen;
