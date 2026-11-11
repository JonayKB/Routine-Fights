import React, { FormEvent, useState } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { Button, Field, Input } from "../ui/kit";
import Icon from "../ui/Icon";

const LoginScreen = () => {
  const { token, login, expired } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: string } | null)?.from ?? "/";
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  if (token) return <Navigate to={from} replace />;

  const submit = async (event: FormEvent) => {
    event.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login(email.trim(), password);
      navigate(from, { replace: true });
    } catch (e: any) {
      setError(e?.message ?? "Could not sign in");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-sm">
        <div className="flex flex-col items-center text-center mb-8">
          <img src="/Logo.png" alt="" className="w-16 h-16 rounded-2xl shadow-card" />
          <h1 className="font-display text-2xl font-semibold mt-5">Routine Fights</h1>
          <p className="text-sm text-ink-2 mt-1">Administration panel</p>
        </div>

        <form onSubmit={submit} className="bg-card border border-ink/10 rounded-2xl shadow-card p-6 flex flex-col gap-4">
          {expired && !error && (
            <p className="flex items-center gap-2 text-sm text-ink-2 bg-raised rounded-xl px-3 py-2.5">
              <Icon name="alert" size={16} />
              Your session has ended. Sign in again.
            </p>
          )}
          <Field label="Email" htmlFor="email">
            <Input
              id="email"
              type="email"
              autoComplete="username"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoFocus
            />
          </Field>
          <Field label="Password" htmlFor="password">
            <Input
              id="password"
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </Field>
          {error && (
            <p role="alert" className="flex items-center gap-2 text-sm text-danger">
              <Icon name="alert" size={16} />
              {error}
            </p>
          )}
          <Button type="submit" loading={loading} className="mt-1 w-full">
            Sign in
          </Button>
        </form>
        <p className="text-xs text-ink-3 text-center mt-4">Only administrator accounts can sign in.</p>
      </div>
    </div>
  );
};

export default LoginScreen;
