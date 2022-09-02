import React, {useEffect} from 'react';
import {ToastContainer, toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {createRoot} from 'react-dom/client';

function App() {

    useEffect(() => {
        const streamLocation = new EventSource('/api/tweetEvent');

        streamLocation.addEventListener('newTweet', function (event) {

            console.log("Fire Event")
            toast.success(event.data, {
                position: toast.POSITION.TOP_RIGHT,
                className: "alert alert-success"
            });

        });
    }, []);

    return (
        <ToastContainer/>
    );
}

const container = document.getElementById('react');
const root = createRoot(container); // createRoot(container!) if you use TypeScript
root.render(<App/>);