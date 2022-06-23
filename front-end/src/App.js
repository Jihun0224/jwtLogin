import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from './pages/Main'
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import './css/main.css';

function App() {
  return (
    <div className="wrap">
      <h1>JWT Ex</h1>

      <BrowserRouter>
        <Routes>
          <Route path="/" exact element={<Main />} />
          <Route path="/login" exact element={<Login />} />
          <Route path="/signup" exact element={<SignUp />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
