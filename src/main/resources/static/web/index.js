const app = Vue.createApp({
    data(){
        return{
            clientName: [],
            clientLastName: [],
            clientEmail: [],
            clientPassword: [],
        }
    },
    created(){
    },
    mounted(){

    },
    methods: {
        register(){
            axios.post('/api/clients', `firstName=${this.clientName}&lastName=${this.clientLastName}&email=${this.clientEmail}&password=${this.clientPassword}`)
            .then(response => this.login())
            .catch(function (error) {
                console.log(error.response.data);
                let errorData = error.response.data
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `Looks like you're ${errorData} field.`,
                })
            })
        },
        login(){
            axios.post('/api/login',`email=${this.clientEmail}&password=${this.clientPassword}`)
            .then(response =>window.location.assign("./accounts.html"))
            .catch (response => Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Wrong email or password! Please try again.',
                footer: '<a href="">Why do I have this issue?</a>'
            })
            );
        },
    },
    computed:{
    },
}).mount('#app')