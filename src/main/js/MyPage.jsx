import React from 'react';
import { Fetch, UrlConfig, AppConfig } from '@jenkins-cd/blueocean-core-js';

export class MyPage extends React.Component {
    componentWillMount() {
        Fetch.fetchJSON(`${UrlConfig.getRestBaseURL()}/organizations/${AppConfig.getOrganizationName()}/awesome/data`)
        .then(data => {
            this.setState({data: data.data});
        });
    }
    render() {
        if (!this.state || !this.state.data) return null;
        return <div className="awesome-page">{this.state.data}</div>;
    }
}
