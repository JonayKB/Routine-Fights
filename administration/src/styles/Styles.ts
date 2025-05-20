const styles: { [key: string]: React.CSSProperties } = {
  page: {
    fontFamily: "Arial, sans-serif",
    backgroundColor: "#f4f4f9",
    color: "#333",
    minHeight: "100vh",
    padding: "2vw 1vw", 
  },
  container: {
    maxWidth: "95vw", 
    margin: "0 auto",
    background: "#ffffff",
    borderRadius: "10px",
    boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
    overflow: "hidden",
    width: "100%",
  },
  header: {
    background: "rgb(130, 76, 175)",
    color: "#fff",
    padding: "2vw 1vw", 
    textAlign: "center",
    fontSize: "clamp(1.2rem, 3vw, 2rem)", 
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))", 
    gap: "1vw",
    padding: "1vw", 
  },
  hamburger: {
    position: "fixed",
    top: "10px",
    left: "10px",
    fontSize: "1.5rem",
    background: "transparent",
    border: "none",
    cursor: "pointer",
    zIndex: 999,
  },

  overlay: {
    position: "fixed",
    top: 0,
    left: 0,
    height: "100vh",
    width: "100vw",
    backgroundColor: "rgba(0, 0, 0, 0.4)",
    zIndex: 1000,
  },

  sidebar: {
    position: "fixed",
    top: 0,
    left: 0,
    height: "100%",
    width: "200px", 
    backgroundColor: "rgb(130, 76, 175)",
    transition: "transform 0.3s ease",
    zIndex: 1001,
    boxShadow: "2px 0 8px rgba(0,0,0,0.2)",
    overflowY: "auto",
  },

  sidebarContent: {
    display: "flex",
    flexDirection: "column",
    gap: "8px",
    padding: "12px",
    paddingTop: "40px",
  },

  link: {
    color: "#fff",
    backgroundColor: "rgba(255, 255, 255, 0.1)",
    padding: "8px 10px", 
    borderRadius: "8px",
    textDecoration: "none",
    fontSize: "16px", 
    transition: "background-color 0.3s ease, transform 0.2s ease",
    cursor: "pointer",
  },

  closeButton: {
    position: "absolute",
    top: "8px",
    right: "8px",
    background: "transparent",
    border: "none",
    color: "#fff",
    fontSize: "20px", // Ajuste de tamaño
    cursor: "pointer",
    zIndex: 1002,
  },
  graphGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
    gap: "12px", 
    padding: "12px",
  },

  graphCard: {
    background: "#e6e6f0",
    borderRadius: "10px",
    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
    padding: "12px", // Ajuste de padding
    textAlign: "center",
    transition: "transform 0.2s ease, background-color 0.2s ease",
    cursor: "pointer",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem", // Ajuste de gap
    padding: "1rem", // Ajuste de padding
  },
  formGroup: {
    display: "flex",
    flexDirection: "column",
  },
  label: {
    marginBottom: "0.3rem", // Ajuste de margen
    fontWeight: "600",
    color: "#444",
    fontSize: "0.95rem", // Ajuste de tamaño de fuente
  },
  input: {
    padding: "0.5rem 0.8rem", // Ajuste de padding
    fontSize: "0.95rem", // Ajuste de tamaño de fuente
    borderRadius: "6px",
    border: "1px solid #ccc",
    outline: "none",
    transition: "border-color 0.2s ease",
  },
  inputFocus: {
    borderColor: "rgb(130, 76, 175)",
  },
  button: {
    padding: "0.5rem 0.8rem", // Ajuste de padding
    fontSize: "0.95rem", // Ajuste de tamaño de fuente
    borderRadius: "6px",
    border: "none",
    backgroundColor: "rgb(130, 76, 175)",
    color: "#fff",
    fontWeight: "700",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
  },
  buttonHover: {
    backgroundColor: "rgb(110, 60, 155)",
  },
  errorText: {
    color: "red",
    marginTop: "0.5rem", // Ajuste de margen
    fontWeight: "600",
  },
  communityEventCard: {
    borderRadius: "1rem",
    padding: "1rem", // Ajuste de padding
    color: "white",
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-end",
    height: "350px", // Ajuste de altura
    maxWidth: "100%",
    boxShadow: "0 8px 16px rgba(0, 0, 0, 0.3)",
    margin: "1rem 0", // Ajuste de margen
    position: "relative",
    backgroundSize: "cover",
    backgroundPosition: "center",
    backgroundRepeat: "no-repeat",
    width: "95vw", // Ajuste de ancho
  },

  communityEventOverlay: {
    backgroundColor: "rgba(0, 0, 0, 0.6)",
    borderRadius: "1rem",
    padding: "1rem", // Ajuste de padding
    height: "100%",
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-end",
    backdropFilter: "blur(2px)",
  },

  communityEventTitle: {
    fontSize: "clamp(1.2rem, 3vw, 2rem)", // Ajuste de tamaño de fuente
    fontWeight: "bold",
    marginBottom: "0.5rem", // Ajuste de margen
    textShadow: "1px 1px 5px rgba(0,0,0,0.5)",
  },

  communityEventText: {
    fontSize: "1rem", // Ajuste de tamaño de fuente
    lineHeight: "1.4", // Ajuste de line-height
    textShadow: "1px 1px 3px rgba(0,0,0,0.5)",
    marginBottom: "0.3rem", // Ajuste de margen
  },

  communityEventBadgeWrapper: {
    position: "relative",
    width: "2.5vw",
    height: "2.5vw", 
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexDirection: "column",
  },

  communityEventBadge: {
    width: "2.5vw",
    height: "2.5vw",
    objectFit: "contain",
    borderRadius: "50%",
    border: "2px solid white",
    backgroundColor: "rgba(255, 255, 255, 0.3)",
    boxShadow: "0 4px 10px rgba(0,0,0,0.3)",
    textAlign: "center",
  },

  communityEventBadgeText: {
    position: "absolute",
    bottom: "-1.5rem",
    fontSize: "0.85rem",
    color: "#fff",
    fontWeight: "bold",
    textShadow: "1px 1px 3px rgba(0,0,0,0.7)",
  },
    communityEventBadgeContainer: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "center",
    gap: "1.5rem",
    padding: "2rem 3rem",
    backgroundColor: "rgba(255, 255, 255, 0.2)",
    borderRadius: "0 0 1rem 1rem",
    marginTop: "auto",
    alignItems: "center",
    width: "100%",
  },

  communityEventAddButton: {
    padding: "0.75rem 1rem",
    fontSize: "1rem",
    borderRadius: "6px",
    border: "none",
    backgroundColor: "#824CAF",
    color: "#fff",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
    marginLeft: "1rem",
  },

  modalOverlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100vw",
    height: "100vh",
    backgroundColor: "rgba(0,0,0,0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 2000,
  },

  modalContent: {
    backgroundColor: "#fff",
    borderRadius: "10px",
    padding: "2rem",
    width: "90%",
    maxWidth: "400px",
    boxShadow: "0 8px 16px rgba(0,0,0,0.3)",
    position: "relative",
  },

  modalFormGroup: {
    display: "flex",
    flexDirection: "column",
    marginBottom: "1rem",
  },

  modalInput: {
    padding: "0.75rem 1rem",
    fontSize: "1rem",
    borderRadius: "6px",
    border: "1px solid #ccc",
    outline: "none",
    marginTop: "0.5rem",
  },

  modalSubmitButton: {
    padding: "0.75rem 1rem",
    fontSize: "1rem",
    borderRadius: "6px",
    border: "none",
    backgroundColor: "#4CAF50",
    color: "#fff",
    cursor: "pointer",
    width: "100%",
  },

  modalCloseButton: {
    position: "absolute",
    top: "0.5rem",
    right: "0.5rem",
    background: "transparent",
    border: "none",
    fontSize: "1.5rem",
    cursor: "pointer",
  },

};
export default styles;
