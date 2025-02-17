import {Link} from "react-router-dom";
import Signup from "./Signup";

const Home = () => {


    return (
        <>
            <div>خوش آمدید</div>
            <Link to="/Login" >
                <button>Login</button>
            </Link>  <Link to="/Signup" >
            <button>Sing Up</button>
        </Link>
        </>



    );
};

export default Home;
