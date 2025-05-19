import React, { FormEvent, useContext, useState } from "react";
import { MainContext } from "../components/MainContext";
import { AuthRepository } from "../repositories/AuthRepository";
import styles from "../styles/Styles";


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
    <div style={styles.page}>
      <div style={styles.container}>
        <h2 style={styles.header}>Login</h2>
        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.formGroup}>
            <label htmlFor="email" style={styles.label}>Email</label>
            <input
              id="email"
              type="email"
              name="email"
              placeholder="Enter your email"
              style={styles.input}
              required
            />
          </div>
          <div style={styles.formGroup}>
            <label htmlFor="password" style={styles.label}>Password</label>
            <input
              id="password"
              type="password"
              name="password"
              placeholder="Enter your password"
              style={styles.input}
              required
            />
          </div>
          <button type="submit" style={styles.button}>Login</button>
        </form>
        {error && <p style={styles.errorText}>{error}</p>}
      </div>
    </div>
  );
};

export default LoginScreen;
