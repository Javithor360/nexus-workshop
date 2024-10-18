$(document).ready(async function () {
    const userToken = await getTokenRequest();
    const projectId = $.getProjectIdFromURL();

    const activities = await $.getProjectActivities(projectId, userToken);
    const projectStatus = $('#status-container span').text();

    $.updateButtonStates(activities, projectStatus);

    $(document).on('click', "#submitLogBtn", (async function () {
        if ($.validateClientForm()) {
            const activityData = $.getFormData(projectId, 'new-log-form');

            if (activityData) {
                await $.createActivity(activityData, userToken);
            }
        } else {
            $.toast({
                text: "A validation error has occurred. Please try again",
                heading: 'Oops! An error has occurred',
                icon: 'error',
                showHideTransition: 'fade',
                allowToastClose: true,
                hideAfter: 3000,
                stack: false,
                loader: false,
                position: 'top-right',
            });
        }
    }));

    // Open the modal
    $('#openModalBtn').click(function () {
        $.toggleModal();
    });

    // Close the modal
    $('#closeModalBtn').click(function () {
        $.toggleModal();
    });

    // Close modal when clicking outside of it
    $('#modal').click(function (event) {
        if ($(event.target).is('#modal')) {
            $(this).addClass('hidden');
            $.resetClientForm();
        }
    });
});

$.extend({ // Creating a repository of utils functions to use in this file

    // ============ FORM UTILS METHODS ============
    validateClientForm: function () { // Function to validate the form
        $("#new-log-form").validate({
            errorElement: 'div',
            rules: {
                title: {
                    required: true
                },
                description: {
                    required: true
                },
                progress: {
                    required: true,
                    range: [0, 100]
                },
                type: {
                    required: true
                }
            },
            messages: {
                progress: {
                    number: "Progress must be a number",
                    range: "Progress must be between 0 and 100"
                }
            }
        });

        return $("#new-log-form").valid();
    },

    getFormData: function (projectId, formId) { // Function to create the object to be sent in the request
        const formData = $(`#${formId}`).serializeArray().reduce((acc, {name, value}) => {
            acc[name] = value; // Assigns each field to an object
            return acc;
        }, {});

        const now = new Date();
        const date = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();

        return {
            projectId: projectId,
            user: {id: formData.userId},
            title: formData.title,
            description: formData.description,
            percentage: formData.progress,
            type: {id: formData.type},
            createdAt: date
        };
    },

    resetClientForm: function () {
        $('#new-log-form')[0].reset(); // Clearing all the inputs
        $('input#id').val(''); // Clearing the id in the hidden input

        $('.form-input input').each(function () { // For each input this function is applied to delete the class 'has-value'
            if ($(this).val().length === 0) {
                $(this).parent().removeClass('has-value');
            }
        });
    },

    toggleModal: function () {
        $('#modal').toggleClass('hidden');
        $.resetClientForm();
    },

    getProjectIdFromURL() {
        const url = window.location.pathname; // Get the current URL path
        const parts = url.split('/'); // Split the URL by '/'
        return parts[parts.length - 1]; // Get the last part, which is the project ID
    },

    updateButtonStates(activities, projectStatus) {
        const lastActivity = activities[activities.length - 1];
        const lastActivityProgress = lastActivity ? lastActivity.percentage : 0;
        const addNewLogBtn = $('#openModalBtn');

        const openProjectStatuses = ['Pending', 'In Progress'];

        if (lastActivityProgress === 100 || !openProjectStatuses.includes(projectStatus)) {
            addNewLogBtn.prop('disabled', true);
            addNewLogBtn.prop('hidden', true)
        } else {
            addNewLogBtn.prop('disabled', false);
            addNewLogBtn.prop('hidden', false)
        }
    },

    // ============ API METHODS ============
    createActivity: async function (activityData, userToken) {
        $.ajax({
            url: "http://localhost:8081/api/activities", // Server URL
            type: "POST", // Method
            data: JSON.stringify(activityData), // Converts the object to a JSON string
            headers: {
                "Authorization": "Bearer " + userToken,
                "Content-Type": "application/json"
            },
            success: async function () {
                const updatedActivities = await $.getProjectActivities(activityData.projectId, userToken);
                $.updateButtonStates(updatedActivities, $('#status-container span').text());

                $.toggleModal(); // Closes the modal
                $.toast({
                    text: "Activity created successfully",
                    heading: 'Success',
                    icon: 'success',
                    showHideTransition: 'fade',
                    allowToastClose: true,
                    hideAfter: 3000,
                    stack: false,
                    loader: false,
                    position: 'top-right',
                });
            }
        });
    },

    getProjectActivities: function (projectId, userToken) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: `http://localhost:8081/api/activities/project/${projectId}`,
                type: "GET",
                headers: {
                    "Authorization": "Bearer " + userToken,
                },
                success: function (activities) {
                    const tbody = $('#activitiesTable tbody');
                    tbody.empty();

                    activities.forEach(activity => {
                        const newRow = `
                            <tr class="border-b hover:bg-neutral-100">
                                <th scope="row" class="px-6 py-4 capitalize">${activity.user.username}</th>
                                <td class="px-6 py-4 capitalize">${activity.title}</td>
                                <td class="px-6 py-4 capitalize">${activity.description}</td>
                                <td class="px-6 py-4 capitalize">${activity.percentage}</td>
                                <td class="px-6 py-4 capitalize">${activity.type.name}</td>
                                <td class="px-6 py-4">${activity.createdAt}</td>
                            </tr>
                        `;
                        tbody.append(newRow);
                    });

                    resolve(activities);
                },
                error: function (error) {
                    reject(error);
                }
            });
        });
    }
});