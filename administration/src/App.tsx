import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import LoginScreen from "./screens/LoginScreen";
import Navbar from "./components/Navbar";
import MainContextProvider from "./components/MainContext";
import HomeScreen from "./screens/HomeScreen";
import CommunityScreen from "./screens/CommunityScreen";

function App() {
  return (
    <BrowserRouter>
      <MainContextProvider>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomeScreen />} />
          <Route path="/login" element={<LoginScreen />} />
          <Route path="/communityEvents" element={<CommunityScreen />} />
          <Route path="*" element={<div>404 Not Found</div>} />
        </Routes>
      </MainContextProvider>
    </BrowserRouter>
  );
}

export default App;
