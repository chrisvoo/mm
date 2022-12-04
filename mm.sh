# @TODO: integrate this into mm CLI

ENV=$1
COMMAND=$2
RMVOLUMES=$3
DC_FILE=""

if [ $# -lt 2 ]
  then
    echo "Not enough arguments supplied"
    exit 1
fi

while [ -n "$1" ]
do
    case "$1" in
        test) DC_FILE="docker-compose-test.yml"
              shift ;;
        dev) DC_FILE="docker-compose-dev.yml" 
             shift ;;
        prod) DC_FILE="docker-compose-prod.yml"
              shift ;;
        rmvol) shift ;;      
        *) echo "Option $1 wrong"; exit 1;;
    esac
shift
done

if [ "$COMMAND" = "start" ]; then
  docker-compose -f $DC_FILE up --build --quiet-pull --remove-orphans -d
elif [ "$COMMAND" = "stop" ]; then
  CMD="docker-compose -f $DC_FILE down --remove-orphans"
  if  [ "$RMVOLUMES" = "rmvol" ]; then
    CMD+=" --volumes"
  fi
  eval $CMD
else
  echo "unknown command"
  exit 1  
fi

