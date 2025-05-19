import React, { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import styles from '../styles/Styles';
import { MainContext } from './MainContext';
import { AuthRepository } from '../repositories/AuthRepository';

const Navbar = () => {
  const [open, setOpen] = useState(false);
  const { token, setToken } = useContext(MainContext);

  const toggleMenu = () => setOpen(!open);
  const closeMenu = () => setOpen(false);
  const handleLogout = () => {
    closeMenu();
    AuthRepository.logout(setToken);

  }

  return (
    <>
      {!open && (
        <button onClick={toggleMenu} style={styles.hamburger}>
          ☰
        </button>
      )}

      {open && <div onClick={closeMenu} style={styles.overlay} />}

      <div
        style={{
          ...styles.sidebar,
          transform: open ? 'translateX(0)' : 'translateX(-100%)',
        }}
      >
        <button onClick={closeMenu} style={styles.closeButton}>
          ✖
        </button>

        <div style={styles.sidebarContent}>
          {!token && (
            <Link to="/login" style={styles.link} onClick={closeMenu} >
              Login
            </Link>
          )}
          <Link to="/" style={styles.link} onClick={closeMenu}>
            Home
          </Link>
          <Link to="/users" style={styles.link} onClick={closeMenu}>
            Users
          </Link>
          {token && (
            <Link to="/login" style={{ ...styles.link, background: "#FF5C5C" }} onClick={handleLogout}>
              Logout
            </Link>
          )}
        </div>
      </div>
    </>
  );
};

export default Navbar;
