import React, { ReactNode, useEffect, useState } from "react";
import { NavLink, useLocation } from "react-router-dom";
import Icon from "./Icon";
import { useAuth } from "../auth/AuthContext";

const NAV = [
  { to: "/", label: "Dashboard", icon: "dashboard" },
  { to: "/events", label: "Community events", icon: "calendar" },
  { to: "/notifications", label: "Notifications", icon: "bell" },
];

const Brand = () => (
  <div className="flex items-center gap-2.5 px-2">
    <img src="/Logo.png" alt="" className="w-8 h-8 rounded-lg" />
    <div className="leading-tight">
      <p className="font-display font-semibold text-ink">Routine Fights</p>
      <p className="text-xs text-ink-3">Administration</p>
    </div>
  </div>
);

const Nav = ({ onNavigate }: { onNavigate?: () => void }) => {
  const { logout } = useAuth();
  return (
    <nav className="flex flex-col h-full">
      <div className="flex flex-col gap-1">
        {NAV.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.to === "/"}
            onClick={onNavigate}
            className={({ isActive }) =>
              `flex items-center gap-3 h-10 px-3 rounded-xl text-sm font-medium transition ${
                isActive ? "bg-accent/10 text-accent-ink" : "text-ink-2 hover:bg-raised hover:text-ink"
              }`
            }
          >
            <Icon name={item.icon} />
            {item.label}
          </NavLink>
        ))}
      </div>
      <button
        onClick={logout}
        className="mt-auto flex items-center gap-3 h-10 px-3 rounded-xl text-sm font-medium text-ink-2 hover:bg-raised hover:text-ink"
      >
        <Icon name="logout" />
        Log out
      </button>
    </nav>
  );
};

/** Sidebar on wide screens; a top bar with a drawer on phones. */
const Shell = ({ children }: { children: ReactNode }) => {
  const [open, setOpen] = useState(false);
  const location = useLocation();
  useEffect(() => setOpen(false), [location.pathname]);

  return (
    <div className="min-h-screen lg:pl-64">
      <aside className="hidden lg:flex fixed inset-y-0 left-0 w-64 flex-col gap-8 px-4 py-6 border-r border-ink/10 bg-card">
        <Brand />
        <Nav />
      </aside>

      <header className="lg:hidden sticky top-0 z-30 flex items-center justify-between h-14 px-4 bg-card/90 backdrop-blur border-b border-ink/10">
        <Brand />
        <button
          onClick={() => setOpen(true)}
          aria-label="Open menu"
          className="p-2 rounded-lg text-ink-2 hover:bg-raised"
        >
          <Icon name="menu" size={22} />
        </button>
      </header>

      {open && (
        <div className="lg:hidden fixed inset-0 z-40">
          <div className="absolute inset-0 bg-black/40" onClick={() => setOpen(false)} />
          <div className="absolute inset-y-0 left-0 w-72 max-w-[85vw] flex flex-col gap-8 bg-card px-4 py-6 shadow-pop">
            <div className="flex items-center justify-between">
              <Brand />
              <button onClick={() => setOpen(false)} aria-label="Close menu" className="p-2 rounded-lg text-ink-2">
                <Icon name="close" />
              </button>
            </div>
            <Nav onNavigate={() => setOpen(false)} />
          </div>
        </div>
      )}

      <main className="mx-auto max-w-6xl px-4 sm:px-8 py-6 sm:py-10">{children}</main>
    </div>
  );
};

export default Shell;
