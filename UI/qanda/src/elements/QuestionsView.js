import React, { useState, useEffect } from 'react';
import axios from 'axios';

function QuestionsView({ questions }) {
    const [userName, setUserName] = useState('');
    const [newQuestion, setNewQuestion] = useState('');
    const [editQuestion, setEditQuestion] = useState({ id: '', text: '' });

    useEffect(() => {
        const userName = localStorage.getItem('username');
        if (userName) {
            setUserName(userName);
        } else {
            setUserName('');
        }
    }, []);
    const handleView = (questionId) => {
        window.location.href = `/qid/${questionId}`;
    };
    const handleAdd = async () => {
        try {
            const response = await axios.post('http://localhost:8082/api/qms/questions', {
                question: newQuestion
            }, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken'),
                    'Content-Type': 'application/json'
                }
            });
            console.log('Question added successfully:', response.data);
            window.location.reload();
        } catch (error) {
            // Handle error
            console.error('Error adding question:', error);
        }
    };

    const handleEdit = async () => {
        try {
            const response = await axios.put(`http://localhost:8082/api/qms/questions/${editQuestion.id}`, {
                question: editQuestion.text
            }, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken')
                }
            });
            console.log('Question edited successfully:', response.data);
            setEditQuestion({ id: '', text: '' }); // Clear edit state
            window.location.reload();
        } catch (error) {
            // Handle error
            console.error('Error editing question:', error);
        }
    };

    const handleDelete = async (questionId) => {
        try {
            const response = await axios.delete(`http://localhost:8082/api/qms/questions/${questionId}`, {
                headers: {
                    'Authorization': localStorage.getItem('jwtToken')
                }
            });
            console.log('Question deleted successfully' + response.data);
            window.location.reload();
        } catch (error) {
            console.error('Error deleting question:', error);
        }
    };

    return (
        <div>
            <div className='home-container'>
                <input type="textarea" value={newQuestion} onChange={(e) => setNewQuestion(e.target.value)} />
                <button onClick={handleAdd}>Add Question</button>
            </div>
            <table className="table">
                <thead>
                    <tr>
                        <th>Question</th>
                        <th>By User</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {questions.map(question => (
                        <tr key={question.questionId}>
                            <td>{question.question}</td>
                            <td>{question.user.username}</td>
                            <td>
                                <button onClick={() => handleView(question.questionId)}>View</button>
                                {userName === question.user.username && (
                                    <>
                                        <button onClick={() => setEditQuestion({ id: question.questionId, text: question.question })}>Edit</button>
                                        <button onClick={() => handleDelete(question.questionId)}>Delete</button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {editQuestion.id && (
                <div>
                    <input type="text" value={editQuestion.text} onChange={(e) => setEditQuestion({ ...editQuestion, text: e.target.value })} />
                    <button onClick={handleEdit}>Save</button>
                </div>
            )}
        </div>
    );
}

export default QuestionsView;
