import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AnswersView from './AnswersView';
import { useParams } from 'react-router-dom';

function Question() {
  const { questionId } = useParams(); // Get questionId from path parameter
  const [question, setQuestion] = useState(null);
  const [answers, setAnswers] = useState([]);

  useEffect(() => {
    if (questionId) {
      fetchQuestionAndAnswers(questionId);
    }
  }, [questionId]);

  const fetchQuestionAndAnswers = async (questionId) => {
    try {
      // Fetch question details based on questionId from URL params
      const questionResponse = await axios.get(`http://localhost:8082/api/qms/questions/${questionId}`);
      setQuestion(questionResponse.data);

      // Fetch answers for the question
      const answersResponse = await axios.get(`http://localhost:8083/api/ams/answers/question/${questionId}`);
      setAnswers(answersResponse.data);
    } catch (error) {
      console.error('Error fetching question and answers:', error);
    }
  };

  return (
    <div className='home-container'>
      {question && (
        <div>
          <h2>Question:</h2>
          <p>{question.question}</p>
        </div>
      )}
      <div>
        <h2>Answers:</h2>
        <AnswersView answers={answers} currentQuestionId={questionId} fetchData={fetchQuestionAndAnswers}/>
        </div>
    </div>
  );
}

export default Question;
