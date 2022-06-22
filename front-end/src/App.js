import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from './pages/Main'
import Login from './pages/Login';
import Register from './pages/Register';

function App() {
  return (
    <div>
      <h1>JWT Jogin</h1>

      <BrowserRouter>
        <Routes>
          <Route path="/" exact element={<Main />} />
          <Route path="/login" exact element={<Login />} />
          <Route path="/register" exact element={<Register />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
