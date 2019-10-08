import React, {Component} from 'react';
import {
    BrowserRouter as Router,
    Route,
    Link
} from 'react-router-dom';

import reactLogo from './images/react.svg';
import playLogo from './images/play.svg';
import javaLogo from './images/java.webp';
import Client from "./Client";

import './App.css';

const Tech = ({match}) => {
    return <div>Current Route: {match.params.tech}</div>
};


class App extends Component {
    constructor(props) {
        super(props);
        this.state = {title: '', files: []};
    }

    async componentDidMount() {
        // Client.getSummary(summary => {
        //     this.setState({
        //         title: summary.content
        //     });
        Client.getFiles(files => {
            this.setState({
                files: files
            })
        });
        // });
    }

    renderTableData() {
        return this.state.files.map((file, index) => {
            const {id, name, createdAt} = file //destructuring
            const downloadUrl = "/file/download/" + id;
            return (
                <tr key={id}>
                    <td>{id}</td>
                    <td>{name}</td>
                    <td>{createdAt}</td>
                    <td><Link to={downloadUrl} target="_blank" download>Download</Link></td>
                </tr>
            )
        })
    }


    render() {
        return (
            <Router>
                <div className="App">
                    <div>
                        <h1 id='title'>Files</h1>
                        <table id='files' border={1}>
                            <thead>
                            <tr>
                                <td>id</td>
                                <td>name</td>
                                <td>created</td>
                                <td></td>
                            </tr>
                            </thead>
                            <tbody>
                            {this.renderTableData()}
                            </tbody>
                        </table>
                    </div>


                    {/*<h1>Welcome to {this.state.title}</h1>*/}
                    {/*<nav>*/}
                    {/*<Link to="java">*/}
                    {/*    <img width="400" height="400" src={javaLogo} alt="Java Logo"/>*/}
                    {/*    </Link>*/}
                    {/*    <Link to="play">*/}
                    {/*    <img width="400" height="400" src={playLogo} alt="Play Framework Logo"/>*/}
                    {/*    </Link>*/}
                    {/*    <Link to="react">*/}
                    {/*    <img width="400" height="400" src={reactLogo} alt="React Logo"/>*/}
                    {/*    </Link>*/}
                    {/*    </nav>*/}
                    {/*    <Route path="/:tech" component={Tech}/>*/}
                    <div>
                        {/*<h2>Check out the project on GitHub for more information</h2>*/}
                        {/*<h3>*/}
                        {/*<a target="_blank" rel="noopener noreferrer" href="https://github.com/yohangz/java-play-react-seed">*/}
                        {/*    java-play-react-seed*/}
                        {/*    </a>*/}
                        {/*    </h3>*/}
                    </div>
                </div>
            </Router>
        );
    }
}

export default App;