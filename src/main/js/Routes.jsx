import React from 'react';
import { Route } from 'react-router';
import { MyPage } from './MyPage';

export default <Route path="/organizations/:organization/awesome" component={MyPage} />;
