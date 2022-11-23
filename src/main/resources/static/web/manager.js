
const app = Vue.createApp({
    data(){
        return{
            clients: [],
            clientName: [],
            clientLastName: [],
            clientEmail: [],
            id: ""
        }
    },
    created(){
        this.loadData()
        this.loadDataRest()
        //.catch(error => console.error(error.message))
    },
    mounted(){

    },
    methods:{
        loadData(){
        axios.get('/api/clients')
        .then((response => {
            this.clients = response.data
        }))
    },

    loadDataRest(){
        axios.get("/rest/clients")
        .then((response => {
            this.clients = response._embedded.clients
        }))
    },
        addClient(){
            if(this.clientName != "" && this.clientLastName != "" && this.clientEmail != ""){
                axios.post('/rest/clients', {
                    firstName: this.clientName,
                    lastName: this.clientLastName,
                    email: this.clientEmail
                })
                .then(() => this.loadData())
                .then(() => {
                    this.clientName = "",
                    this.clientLastName = "",
                    this.clientEmail = ""
                })
            }
        },
    },
    computed:{

    },
}).mount('#app')