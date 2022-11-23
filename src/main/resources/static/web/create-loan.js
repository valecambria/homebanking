const app = Vue.createApp({
    data(){
        return{
            loanName: "",
            loanAmount: 0,
            loanPayments: [],
        }
    },
    created(){
        this.loadData()
    },
    mounted(){

    },
    methods: {
        loadData(url){
            axios.get('/api/clients/current')
            .then((response => {

            }))
        },
        logout(){
            axios.post('/api/logout')
            .then((response =>{
                window.location.assign("./index.html")
            }))
        },
        createLoan(){
            axios.post('/api/loans/create', {"name":this.loanName,"maxAmount":this.loanAmount,"payments":this.loanPayments})
            .then((response =>{
                window.location.reload()
            }))
        },
    },
    computed:{

    },
}).mount('#app')