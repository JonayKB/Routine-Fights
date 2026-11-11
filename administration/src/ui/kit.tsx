import React, { ReactNode, useEffect, useState } from "react";
import Icon from "./Icon";
import { imageUrl } from "../api/admin";

type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: "primary" | "secondary" | "ghost" | "danger";
  icon?: string;
  loading?: boolean;
};

export const Button = ({ variant = "primary", icon, loading, children, className = "", disabled, ...rest }: ButtonProps) => {
  const styles = {
    primary: "bg-accent text-white hover:brightness-110",
    secondary: "bg-card text-ink border border-ink/10 hover:bg-raised",
    ghost: "text-ink-2 hover:bg-raised hover:text-ink",
    danger: "bg-danger text-white hover:brightness-110",
  }[variant];
  return (
    <button
      {...rest}
      disabled={disabled || loading}
      className={`inline-flex items-center justify-center gap-2 h-10 px-4 rounded-xl text-sm font-semibold transition disabled:opacity-50 disabled:cursor-not-allowed ${styles} ${className}`}
    >
      {loading ? <Spinner size={16} /> : icon ? <Icon name={icon} size={16} /> : null}
      {children}
    </button>
  );
};

export const Spinner = ({ size = 20 }: { size?: number }) => (
  <span
    role="status"
    aria-label="Loading"
    style={{ width: size, height: size }}
    className="inline-block rounded-full border-2 border-current border-r-transparent animate-spin"
  />
);

export const Card = ({ children, className = "" }: { children: ReactNode; className?: string }) => (
  <section className={`bg-card border border-ink/10 rounded-2xl shadow-card ${className}`}>{children}</section>
);

export const CardHeader = ({ title, hint, action }: { title: string; hint?: string; action?: ReactNode }) => (
  <div className="flex items-start justify-between gap-4 px-5 pt-5">
    <div>
      <h2 className="font-display text-base font-medium text-ink">{title}</h2>
      {hint && <p className="text-sm text-ink-3 mt-0.5">{hint}</p>}
    </div>
    {action}
  </div>
);

export const PageHeader = ({ title, hint, action }: { title: string; hint?: string; action?: ReactNode }) => (
  <header className="flex flex-wrap items-end justify-between gap-4 mb-6">
    <div>
      <h1 className="font-display text-2xl sm:text-3xl font-semibold text-ink">{title}</h1>
      {hint && <p className="text-sm text-ink-2 mt-1">{hint}</p>}
    </div>
    {action}
  </header>
);

const fieldBase =
  "w-full rounded-xl bg-page border border-ink/10 px-3.5 text-sm text-ink placeholder:text-ink-3 focus:outline-none focus:border-accent-ink focus:ring-2 focus:ring-accent/20 transition";

type FieldProps = { label: string; hint?: ReactNode; error?: string; children: ReactNode; htmlFor?: string };

export const Field = ({ label, hint, error, children, htmlFor }: FieldProps) => (
  <div className="flex flex-col gap-1.5">
    <label htmlFor={htmlFor} className="text-sm font-medium text-ink">
      {label}
    </label>
    {children}
    {error ? <p className="text-xs text-danger">{error}</p> : hint ? <div className="text-xs text-ink-3">{hint}</div> : null}
  </div>
);

export const Input = React.forwardRef<HTMLInputElement, React.InputHTMLAttributes<HTMLInputElement>>(
  ({ className = "", ...rest }, ref) => <input ref={ref} {...rest} className={`${fieldBase} h-10 ${className}`} />
);

export const TextArea = ({ className = "", ...rest }: React.TextareaHTMLAttributes<HTMLTextAreaElement>) => (
  <textarea {...rest} className={`${fieldBase} py-2.5 min-h-[96px] resize-y ${className}`} />
);

export const Modal = ({
  open,
  title,
  onClose,
  children,
  footer,
  wide,
}: {
  open: boolean;
  title: string;
  onClose: () => void;
  children: ReactNode;
  footer?: ReactNode;
  wide?: boolean;
}) => {
  useEffect(() => {
    if (!open) return;
    const onKey = (e: KeyboardEvent) => e.key === "Escape" && onClose();
    document.addEventListener("keydown", onKey);
    document.body.style.overflow = "hidden";
    return () => {
      document.removeEventListener("keydown", onKey);
      document.body.style.overflow = "";
    };
  }, [open, onClose]);
  if (!open) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center p-0 sm:p-6">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />
      <div
        role="dialog"
        aria-modal="true"
        aria-label={title}
        className={`relative w-full ${wide ? "sm:max-w-2xl" : "sm:max-w-md"} max-h-[92vh] flex flex-col bg-card rounded-t-2xl sm:rounded-2xl shadow-pop`}
      >
        <div className="flex items-center justify-between px-5 py-4 border-b border-ink/10">
          <h2 className="font-display text-lg font-medium">{title}</h2>
          <button onClick={onClose} aria-label="Close" className="p-1.5 rounded-lg text-ink-2 hover:bg-raised">
            <Icon name="close" />
          </button>
        </div>
        <div className="px-5 py-5 overflow-y-auto">{children}</div>
        {footer && <div className="flex justify-end gap-2 px-5 py-4 border-t border-ink/10">{footer}</div>}
      </div>
    </div>
  );
};

export const Empty = ({ icon, title, hint, action }: { icon: string; title: string; hint?: string; action?: ReactNode }) => (
  <div className="flex flex-col items-center text-center py-14 px-6">
    <div className="w-12 h-12 rounded-full bg-raised flex items-center justify-center text-ink-2">
      <Icon name={icon} size={22} />
    </div>
    <p className="mt-4 font-medium text-ink">{title}</p>
    {hint && <p className="mt-1 text-sm text-ink-3 max-w-sm">{hint}</p>}
    {action && <div className="mt-5">{action}</div>}
  </div>
);

export const Skeleton = ({ className = "" }: { className?: string }) => (
  <div className={`bg-raised rounded-xl animate-pulse ${className}`} />
);

/** A stored image; it needs the session, so it's fetched before showing it. */
export const StoredImage = ({
  name,
  alt,
  className = "",
  size = "medium",
}: {
  name?: string | null;
  alt: string;
  className?: string;
  size?: "small" | "medium" | "large";
}) => {
  const [src, setSrc] = useState<string | null>(null);
  const [failed, setFailed] = useState(false);
  useEffect(() => {
    let alive = true;
    setSrc(null);
    setFailed(false);
    if (!name) return;
    imageUrl(name, size)
      .then((url) => alive && setSrc(url))
      .catch(() => alive && setFailed(true));
    return () => {
      alive = false;
    };
  }, [name, size]);
  if (!src) {
    return (
      <div className={`bg-raised flex items-center justify-center text-ink-3 ${failed ? "" : "animate-pulse"} ${className}`}>
        {failed && <Icon name="image" />}
      </div>
    );
  }
  return <img src={src} alt={alt} className={`object-cover ${className}`} />;
};
