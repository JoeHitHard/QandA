import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Home.css';
import QuestionsView from './QuestionsView';
import AnswersView from './AnswersView';


function Home({ setIsLoggedIn }) {
    const [userName, setUserName] = useState('');
    const [questions, setQuestions] = useState([]);
    const [answers, setAnswers] = useState([]);

    const fetchData = async (userId) => {
        try {
            const booksResponse = await axios.get(`http://localhost:8082/api/qms/questions/user/${userId}`, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken'),
                    'Content-Type': 'application/json'
                }
            });
            setQuestions(booksResponse.data);

            const booksLentResponse = await axios.get(`http://localhost:8083/api/ams/answers/user/${userId}`, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken'),
                    'Content-Type': 'application/json'
                }
            });
            setAnswers(booksLentResponse.data);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    useEffect(() => {
        const userName = localStorage.getItem('username');
        if (userName) {
            setUserName(userName);
        } else {
            setUserName('');
        }
        const userId = localStorage.getItem('userId');
        fetchData(userId);
    }, []);

    return (
        <div className='home-container'>
            <h1>
                Welcome, {userName}!!!
            </h1>
            <div className='home-container'>
                <div className='section'>
                    <h1>My Questions</h1>
                    {<QuestionsView questions={questions}/>}
                </div>
                <div className='section'>
                    <h1>My Answers</h1>
                    {<AnswersView answers={answers}/>}
                </div>
            </div>
        </div>
    );
}

export default Home;