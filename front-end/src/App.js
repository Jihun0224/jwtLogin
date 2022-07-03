import { Component, useState } from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from './pages/Main'
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import MyPage from './pages/MyPage';
import './css/main.css';

const App = () => {
  const { Kakao } = window;
  const [token, setToken] = useState(null);
  const handleLogin = (token) => {
    setToken(token);
  };
  const handleLogout = () => {
    Kakao.Auth.logout(function () {
      setToken(null);
    })
  };

  return (
    <div className="wrap">
      <h1>JWT Ex</h1>
      <BrowserRouter>
        <Routes>
          <Route path="/" exact element={<Main onLogout={handleLogout} token={token} />} />
          <Route path="/login" exact element={<Login onLogin={handleLogin} />} />
          <Route path="/signup" exact element={<SignUp />} />
          <Route path="/mypage" exact element={<MyPage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );

}

export default App;
