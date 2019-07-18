import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
    {path: '', redirectTo: '/order', pathMatch: 'full'},
];

export const AppRouting = RouterModule.forRoot(routes);
