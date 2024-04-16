import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './elements/Login';
import Home from './elements/Home';
import Question from './elements/Question';


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const jwtToken = localStorage.getItem('jwtToken');
    if (jwtToken) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false);
    }
  }, []);

  return (
    <Router>
      <Routes>
      {isLoggedIn !== true && <Route path="/" element={<Login setIsLoggedIn={setIsLoggedIn} />} />}
      {isLoggedIn === true &&  <Route path="/" element={<Home />}/>}
      {isLoggedIn === true &&  <Route path="/qid/:questionId" element={<Question />} />}
      </Routes>
    </Router>
  );
}

export default App;