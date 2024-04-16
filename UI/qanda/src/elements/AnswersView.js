import React, { useState, useEffect } from 'react';
import axios from 'axios';

function AnswersView({ answers, currentQuestionId}) {
    const [userName, setUserName] = useState('');
    const [newAnswer, setNewAnswer] = useState('');
    const [editAnswer, setEditAnswer] = useState({ id: '', text: '' });

    useEffect(() => {
        const userName = localStorage.getItem('username');
        if (userName) {
            setUserName(userName);
        } else {
            setUserName('');
        }
    }, []);

    const handleAdd = async () => {
        try {
            const response = await axios.post(`http://localhost:8083/api/ams/answers/${currentQuestionId}`, {
                answer: newAnswer,
            }, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken'),
                    'Content-Type': 'application/json'
                }
            });
            console.log('Answer added successfully:', response.data);
            window.location.reload();
        } catch (error) {
            console.error('Error adding answer:', error);
        }
    };

    const handleEdit = async () => {
        try {
            const response = await axios.put(`http://localhost:8083/api/ams/answers/${editAnswer.id}`, {
                answer: editAnswer.text
            }, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken')
                }
            });
            console.log('Answer edited successfully:', response.data);
            window.location.reload();
            setEditAnswer({ id: '', text: '' }); // Clear edit state
        } catch (error) {
            console.error('Error editing answer:', error);
        }
    };

    const handleDelete = async (answerId) => {
        try {
            const response = await axios.delete(`http://localhost:8083/api/ams/answers/${answerId}`, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken')
                }
            });
            console.log('Answer deleted successfully:', response.data);
            window.location.reload();
        } catch (error) {
            console.error('Error deleting answer:', error);
        }
    };
    const handleView = (questionId) => {
        window.location.href = `/qid/${questionId}`;
    };
    return (
        <div>
            {currentQuestionId && <h1>Answers:</h1>}
            {currentQuestionId && <div className='home-container'>
                <input type="textarea" value={newAnswer} onChange={(e) => setNewAnswer(e.target.value)} />
                <button onClick={handleAdd}>Add Answer</button>
            </div>}
            <table className="table">
                <thead>
                    <tr>
                        <th>Answer</th>
                        <th>By User</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {answers.map(answer => (
                        <tr key={answer.answerId}>
                            <td>{answer.answer}</td>
                            <td>{answer.user.username}</td>
                            <td>
                                <button onClick={() => handleView(answer.question.questionId)}>View</button>
                                {userName === answer.user.username && (
                                    <>
                                        <button onClick={() => setEditAnswer({ id: answer.answerId, text: answer.answer })}>Edit</button>
                                        <button onClick={() => handleDelete(answer.answerId)}>Delete</button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {editAnswer.id && (
                <div>
                    <input type="text" value={editAnswer.text} onChange={(e) => setEditAnswer({ ...editAnswer, text: e.target.value })} />
                    <button onClick={handleEdit}>Save</button>
                </div>
            )}
        </div>
    );
}

export default AnswersView;
