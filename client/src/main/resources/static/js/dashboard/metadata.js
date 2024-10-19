$(document).ready(async function(){
    let userToken = await getTokenRequest();

    await $.loadDataDashboardBoss(userToken, url, idUser);
})

$.extend({ // Creating a repository of utils functions to use in dashboard

    loadDataDashboardBoss: function (userToken, url, id){

        let endpoint = 'http://localhost:8081/api/dashboard-tool/';
        endpoint +=  id != null ? url + '/' + id : url;

        $.ajax({
            url: endpoint, // Server URL
            method: 'GET', // Method
            headers: {
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function (data){
                // Update dashboard data
                console.log(data);
                $('#projects-progress').text(data.totalInProgressProjects);
                $('#projects-completed').text(data.totalCompletedProjects);

                // Update list of recent activities
                $.loadActivity(data.top5Activities);

                // update task list
                $.loadTasks(data.top5Projects);

            }, error: function (response){
                $.toast({ // Toast indicating warning of deleting to the client
                    text: "We have trouble collecting your information",
                    heading: 'Warning!',
                    icon: 'warning',
                    showHideTransition: 'fade',
                    allowToastClose: true,
                    hideAfter: 5000,
                    stack: false,
                    loader: false,
                    position: 'top-right',
                });
            }
        })
    },

    loadActivity: function (activities){
        const activitiesList = $('#activities-list');

        if (activities.length === 0){
            const empty = `
                <li class="w-full p-6 text-center">
                    <div>
                        <span class="material-symbols-outlined text-teal-900 !text-6xl">
                        event_available
                        </span>
                    </div>
                    <h3 class="text-lg text-teal-900 font-bold">You don't have any activities yet!!</h3>
                    <p class="text-sm text-slate-400">Finish projects or check if you've been assigned one</p>
                    <div class=" w-1/2 mx-auto">
                        <a href="http://localhost:8080/dashboard/${url}/management/project" class="style-btn-submit mt-4 block">Go projects</a>
                    </div>
                </li>
            `;

            activitiesList.append(empty);
            return;
        }

        const options = { // Options to convert date
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        }

        activities.forEach(activity =>{
            let date = new Date(activity.createdAt);// instance of date od craeted at

            let formatedDate = date.toLocaleDateString('es', options)

            const newItem = `
                <li class="item-historial">
                    <div
                       class="absolute transform translate-x-[-0.45rem] translate-y-4 bg-yellow-500 w-3 h-3 rounded-full z-10">
                    </div>
                    <div class="ml-6 mb-4">
                        <p class="text-gray-500 text-xs leading-none">${formatedDate}</p>
                        <h3 class="font-bold text-sm capitalize-first text-lowercase"><span class="">${activity.user.role.name}</span> ${activity.user.username}: ${activity.description}</h3>
                        <h2 class="font-bold text-yellow-500 uppercase">${activity.title}</h2>
                   </div>
                </li>
            `;

            activitiesList.append(newItem);

        })
    },

    loadTasks: function (tasks){

        const listTasks = $('#tasks-list'); // Task list from html

        if (tasks.length === 0){

            const empty = `
                <div class="w-full p-6 text-center">
                    <div>
                        <span class="material-symbols-outlined text-teal-900 !text-6xl">
                        event_available
                        </span>
                    </div>
                    <h3 class="text-lg text-teal-900 font-bold">You don't have any projects assigned yet!!</h3>
                    <p class="text-sm text-slate-400">Don't worry, wait for your next assignments</p>
                    <div class=" w-1/2 mx-auto">
                        <a href="http://localhost:8080/dashboard/${url}/management/project" class="style-btn-submit mt-4 block">Go projects</a>
                    </div>
                </div>
            `;

            listTasks.append(empty);
            return;
        }

        tasks.forEach(task =>{
            // Create a new date object from string
            let date = new Date(task.dueDate);
            // Using Intl.DateTimeFormat to format the date
            let formatedDate = new Intl.DateTimeFormat('es', {
                day: '2-digit', // Display day with 2 digits (01-31)
                month: '2-digit', // Display month with 2 digits (01-12)
                year: 'numeric' // Display full year (2024)
            }).format(date).replace(/-/g, '/') // Replace hyphens with slashes

            const newTask = `
                <div class="p-3 border border-slate-500 rounded-lg">
                    <div class="flex justify-between">
                        <span class="font-bold">${task.title}</span>
                        <span class="text-yellow-500 font-bold">${formatedDate}</span>
                    </div>
                    <div class="flex justify-between">
                        <span class="text-xs xl:text-sm text-gray-400">Cliente: ${task.client.name}</span>
                        <span class="text-xs xl:text-sm text-gray-400">Fecha entrega</span>
                    </div>
                </div>
            `;

            listTasks.append(newTask);
        })
    }

})