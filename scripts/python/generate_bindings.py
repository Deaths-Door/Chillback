import os
import subprocess
import shutil
def build_and_generate_kotlin_bindings() :
    script_dir = os.path.dirname(os.path.abspath(__file__))
    chillback_project_dir = os.path.dirname(os.path.dirname(script_dir))
    backend_dir = os.path.join(chillback_project_dir,"backend")
    
    try :
        # Build the Rust project in release mode
        subprocess.run(["cargo", "build", "--release"], cwd=backend_dir, check=True)
    except subprocess.CalledProcessError as e:
        print("Error during building backend in release mode", e)


    output_dir = os.path.join(chillback_project_dir,"app/src/main/kotlin/com/deathsdoor/chillback/backend")
    try :
        # Generate Kotlin bindings
        subprocess.run([
            "cargo", "run", "--bin", "uniffi-bindgen",
            "generate",
            "--library", "target/release/libbackend.so",
            "--language", "kotlin",
            "--out-dir", output_dir
        ], cwd=backend_dir, check=True) 

        # Move file to where it is excepted

        shutil.move(os.path.join(output_dir,"uniffi/backend/backend.kt"),os.path.join(output_dir,"backend.kt"))

        shutil.rmtree(os.path.join(output_dir,"uniffi"))
    except subprocess.CalledProcessError as e:
        print("Error generating kotlin bindings",e)



    # Fix compile errors in generated bindings
    with open(os.path.join(output_dir, "backend.kt"), "r+") as file:
        contents = file.read().replace("``","`_`")

        # Move file pointer back to the beginning to overwrite
        file.seek(0)
        file.write(contents)

        # Truncate to ensure file size matches new content
        file.truncate()  
 
    # Install cross and add the required rust targets in parallel
    #   cargo install cross --git https://github.com/cross-rs/cross
    #   rustup target add armv7-linux-androideabi  
    #   rustup target add i686-linux-android
    #   rustup target add aarch64-linux-android     
    #   rustup target add x86_64-linux-android      
    child_process : list[subprocess.Popen] = []

    TARGETS = ["armv7-linux-androideabi","i686-linux-android"] 

    for target in TARGETS :
        child_process.append(subprocess.Popen(["rustup","target","add",target],cwd=backend_dir))

    child_process.append(subprocess.Popen(["cargo", "install", "cross", "--git", "https://github.com/cross-rs/cross"]))

    for proccess in child_process :
        try : proccess.wait()
        except subprocess.CalledProcessError as e:
            print("Error installing or setting up prequistes",e)

    child_process.clear()

    for target in TARGETS :
        child_process.append(subprocess.Popen(["cross","build","--target",target],cwd=backend_dir))

    for index ,proccess in enumerate(child_process) :
        try : proccess.wait()
        except subprocess.CalledProcessError as e:
            print(f"Error building for target ({TARGETS[index]}) with {e}")

    child_process.clear()
    #TODO : also comment / uncommnet this line openssl = { version = "0.10", features = ["vendored"] } in cargo.toml so that compiling works
    # TODO : move this generated shit to jnilibs correct subdir
            


if __name__ == "__main__" :
    build_and_generate_kotlin_bindings()