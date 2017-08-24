import React from 'react';
import { Fetch, UrlConfig, AppConfig } from '@jenkins-cd/blueocean-core-js';
import { Dialog } from '@jenkins-cd/design-language';

export class MyPage extends React.Component {
    componentWillMount() {
        Fetch.fetchJSON(`${UrlConfig.getRestBaseURL()}/organizations/${AppConfig.getOrganizationName()}/awesome/`)
        .then(response => {
            this.setState({message: response.message});
        });
    }
    render() {
        if (!this.state || !this.state.message) return null;
        return (
            <Dialog buttons={[]} title="Message" className="Dialog--input Dialog--medium-size" >
                <div className="awesome-page">{this.state.message}</div>
            </Dialog>
        );
    }
}
